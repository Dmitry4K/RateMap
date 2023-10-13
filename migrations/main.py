import os
import time
from typing import Mapping, Any

import pymongo
from numpy import pi, cos, sin, arccos, arange, arcsin, sqrt, sign, column_stack
from pymongo import MongoClient

import matplotlib.pyplot as pp

MONGO_URL = "mongodb://admin:admin@localhost:27017/"  # os.environ["MONGO_URL"]
BATCH_SIZE = 10_000
POINTS_COUNT = 250_000_000
# count and distance (meters)
# 100_000      50_000 (50km) 1 or 2 nearest points
# 10_000_000 8000-3 nearest
# 250_000_000
#
# 10_000_000 10000 level of regions 6 nearest
# 70_000_000 3000  level of towns
# 1000  level of districts
# 250   level of city blocks
# 50    level of buildings

def main():
    client = connect()
    migrate(client)
    points(client)
    close(client)

def connect():
    client = MongoClient(MONGO_URL)
    return client


def migrate(client):
    tablename = "points"
    db = client["local"]
    colla = {'locale': 'en_US', 'strength': 2, 'numericOrdering': True, 'backwards': False}
    if tablename not in db.list_collection_names():
        print(f"Creating collection {tablename} ...")
        db.create_collection(
            name=tablename,
            codec_options=None,
            read_preference=None,
            write_concern=None,
            read_concern=None,
            session=None,
            collation=colla
        )
    else:
        print(f"Collection {tablename} is already exists.")

    collection = db[tablename]
    types = [x['key'][0][1] for x in collection.index_information().values()]
    if '2dsphere' not in types:
        print("Creating geospatial index ...")
        resp = collection.create_index([("point.coordinates", pymongo.GEOSPHERE)])
        print(f"Index {resp} created.")
    else:
        print(f"Index is already exists.")
    return client


def points(client):
    batch_count = round(POINTS_COUNT / BATCH_SIZE)
    print(f"Starting generating of points at {time.asctime(time.localtime())}")
    for batch_number in range(batch_count):
        print(f"\rGenerating batch {batch_number+1}/{batch_count} with size: {BATCH_SIZE}", end='')
        pts = batch(batch_number, BATCH_SIZE, POINTS_COUNT)
        load(client, pts)
    print(f"All points was generated at {time.asctime(time.localtime())}")


def close(client):
    client.close()


def batch(batch_number: int, batch_size: int, num_pts: int):
    indices = arange(batch_number * batch_size, (batch_number + 1) * batch_size, dtype=float) + 0.5
    phi = arccos(1 - 2 * indices / num_pts)
    theta = pi * (1 + 5 ** 0.5) * indices % (2 * pi)
    x, y, z = cos(theta) * sin(phi), sin(theta) * sin(phi), cos(phi)
    lat, lng = arcsin(z / sqrt(x * x + y * y + z * z)) * 180 / pi, sign(y) * arccos(x / sqrt(x * x + y * y)) * 180 / pi
    # x, y, z = cos(lat * pi / 180) * cos(lng* pi / 180), cos(lat* pi / 180) * sin(lng* pi / 180), sin(lat* pi / 180)
    # print(max(lat))
    # print(min(lat))
    # print(max(lng))
    # print(min(lng))
    # pp.figure(figsize=(16, 12)).add_subplot(111, projection='3d').scatter(x, y, z)
    # pp.xlabel('X')
    # pp.ylabel('Y')
    # pp.show()
    return lat, lng


def load(client: MongoClient[Mapping[str, Any]], pts):
    lat = pts[0]
    lng = pts[1]
    stacked = column_stack((lat, lng))
    objects = [
        {
            "mark": 0.0,
            "count": 0.0,
            "point": {
                "type": "Point",
                "coordinates": [
                    x[1],
                    x[0]
                ]
            },
            "_class": "ru.dmitry4k.geomarkback.data.dao.GeoPointDao"
        } for x in stacked
    ]
    resp = client["local"]["points"].insert_many(objects)
    # print(f"Successfully inserted {len(resp.inserted_ids)} rows")


if __name__ == '__main__':
    main()
