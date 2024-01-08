import os
import time
import yaml
import numpy as np
import geopy.distance as geodist
import random
from math import ceil
from typing import Mapping, Any

import pymongo
from numpy import pi, cos, sin, arccos, arange, arctan2
from pymongo import MongoClient

# import matplotlib.pyplot as pp

MONGO_USER = os.getenv("MONGO_USER")
MONGO_PASS = os.getenv("MONGO_PASS")
MONGO_DB_URL = os.getenv("MONGO_DB_URL")
MONGO_CRT_PATH = os.getenv("MONGO_CRT_PATH")
MONGO_URL = f"mongodb://{MONGO_USER}:{MONGO_PASS}@{MONGO_DB_URL}/?authMechanism=DEFAULT&tls=true&tlsCAFile={MONGO_CRT_PATH}"
BATCH_SIZE = 10_000
DATABASE_NAME = "ratemap-mongo-db"
RATEMAP_CONFIG_PATH = os.getenv("RATEMAP_CONFIG_PATH")


# count and distance (meters)
# 100_000      50_000 (50km) 1 or 2 nearest points
# 10_000_000 8000-3 nearest
# 250_000_000
#
# 7_000_000         10000 level of regions 6 nearest
# 70_000_000        3000  level of towns
# 650_000_000       1000  level of districts   около 20 Гб данных примерно 3 часа подсчета
# 10_413_794_304    250   level of city blocks Невозможно в лабораторной среде 3.5 ТБ данных, 10 дней подсчета
# 260_344_857_600   50    level of buildings   Невозможно в лабораторной среде 90 Тб данных, 273 дня подсчета

# После получения зависимости количества точек от дистанции между ними удалось посчитать примерное
# количество точек для расчета слоя строений, для него понадобиться расчитать около 262_144_000_000 точек
# на текузий момент расчет 20_000_000 точек занимает около 30 минут, в итоге надо будет потратить около года,
# чтобы расчитать точки для уровня с строениями.

# Можно подумать над ускорением расчета точек, за счет мулти процессорного расчета (в теории при 16 ядерном процессоре
# снизить скорость расчета с 10 дней до 1 дня для слоя с кварталами, однако есть риск того, что MongoDB не выдержит
# такого обхема данных и/или будет бутылочным горлышком при вставке точек при мультипроцессорном расчете точек.

# Возможное улучшение по памяти - не сохранять и/или не считать точки на океанах, т.к. 2/3 поверхности - океаны

# !!!! Возможное улучшение - расчитать точки ТОЛЬКО для России, т.к. ее территория занимает 13% поверхности Земли.
def main():
    config = read_config(RATEMAP_CONFIG_PATH)
    client = connect()
    create_points(client, config)
    close(client)


def read_config(path: str):
    with open(path, "r") as stream:
        try:
            return yaml.safe_load(stream)
        except yaml.YAMLError as exc:
            print(exc)


def connect():
    client = MongoClient(MONGO_URL)
    return client


def create_points(client, config):
    layers = config['layers']
    areas = config['areas']
    for layer in layers:
        for area in areas:
            table_name = area + '_' + layer
            d = layers[layer]['searchDistance']
            pts_count = 16 * 6400 * 6400 * 1000 * 1000 / (d * d)
            batch_size = BATCH_SIZE
            lat = areas[area]['center']['lat']
            lng = areas[area]['center']['lng']
            radius = areas[area]['radius']
            create_table(client, table_name)
            points(client, table_name, pts_count, batch_size, lat, lng, radius)


def create_table(client, table_name):
    db = client[DATABASE_NAME]
    colla = {'locale': 'en_US', 'strength': 2, 'numericOrdering': True, 'backwards': False}
    if table_name not in db.list_collection_names():
        print(f"Creating collection {table_name} ...")
        db.create_collection(
            name=table_name,
            codec_options=None,
            read_preference=None,
            write_concern=None,
            read_concern=None,
            session=None,
            collation=colla
        )
    else:
        print(f"Collection {table_name} is already exists.")

    collection = db[table_name]
    types = [x['key'][0][1] for x in collection.index_information().values()]
    if '2dsphere' not in types:
        print("Creating geospatial index ...")
        resp = collection.create_index([("point.coordinates", pymongo.GEOSPHERE)])
        print(f"Index {resp} created.")
    else:
        print(f"Index is already exists.")
    return client


def points(client, table_name, pts_count, batch_size, lat, lng, radius):
    batch_count = ceil(pts_count / batch_size)
    start = time.time()
    elapsed_points = radius * radius * pts_count / (4 * 6400 * 6400 * 1000 * 1000)
    curr_points_num = 0
    for batch_number in range(batch_count):
        print(
            f"\rGenerated {curr_points_num}({curr_points_num * 100 / elapsed_points:.2f}%) "
            f"points for table: {table_name}, elapsed time: {time.time() - start:.2f}",
            end='')
        pts = batch(batch_number, batch_size, pts_count, lat, lng)
        pts = np.dstack((pts[1], pts[0])).reshape((batch_size, 2))
        pts = filter_batch(pts, radius, lat, lng)
        if len(pts) > 0:
            load(client, pts, table_name)
            curr_points_num += len(pts)
        if len(pts) < batch_size:
            print(
                f"\rGenerated {curr_points_num}({curr_points_num * 100 / elapsed_points:.2f}%) "
                f"points for table: {table_name}, elapsed time: {time.time() - start:.2f}")
            return
    print()


def close(client):
    client.close()

def geo_filter(point, radius, lat, lng):
    return radius > geodist.geodesic((point[1], point[0]), (lat, lng)).m

def filter_batch(pts, radius, lat, lng):
    result = []
    for point in pts:
        if geo_filter(point, radius, lat, lng):
            result.append(point)
    return np.array(result)

def check_batch(pts, radius, lat, lng, pts_check_count) -> bool:
    for _ in range(pts_check_count):
        p = pts[random.randint(0, len(pts)-1)]
        if not geo_filter(p, radius, lat, lng):
            return False
    return True


def batch(batch_number: int, batch_size: int, num_pts: int, lat_center, lng_center):
    indices = arange(batch_number * batch_size, (batch_number + 1) * batch_size, dtype=float) + 0.5
    phi = arccos(1 - 2 * indices / num_pts)
    theta = pi * (1 + 5 ** 0.5) * indices % (2 * pi)
    la = lat_center * pi / 180
    ln = -(lng_center * pi / 180 + pi / 2)
    x, y, z = cos(theta) * sin(phi), sin(theta) * sin(phi), cos(phi)
    x1, y1, z1 = x, cos(la) * y + sin(la) * z, -sin(la) * y + cos(la) * z
    x2, y2, z2 = cos(ln) * x1 - sin(ln) * z1, y1, sin(ln) * x1 + cos(ln) * z1
    lat = (pi / 2 - arccos(y2)) * (180 / pi)
    lng = (pi - arctan2(z2, x2)) * (180 / pi) - 180
    # print(max(lat))
    # print(min(lat))
    # print(max(lng))
    # print(min(lng))
    # pp.ion()
    # pp.figure(figsize=(16, 12)).add_subplot(111, projection='3d')
    # for i in range(len(x)):
    #     pp.subplot(111).scatter(x[:i], y[:i], z[:i])
    #     pp.xlabel('X')
    #     pp.ylabel('Y')
    #     pp.draw()
    #     pp.pause(0.0001)
    return lat, lng


def load(client: MongoClient[Mapping[str, Any]], pts, table_name):
    objects = [
        {
            "mark": random.random() * 5.0,
            "count": 0.0,
            "point": {
                "type": "Point",
                "coordinates": [x[0], x[1]]
            }
        } for x in pts
    ]
    resp = client[DATABASE_NAME][table_name].insert_many(objects)
    # print(f"Successfully inserted {len(resp.inserted_ids)} rows")


if __name__ == '__main__':
    main()
