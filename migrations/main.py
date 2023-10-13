import os

import pymongo
from numpy import pi, cos, sin, arccos, arange, arcsin, sqrt
from pymongo import MongoClient

# import matplotlib.pyplot as pp

MONGO_URL = os.environ["MONGO_URL"]
MONGO_USER = os.environ["MONGO_USER"]
MONGO_PASS = os.environ["MONGO_PASS"]
BATCH_SIZE = int(os.environ["BATCH_SIZE"])
POINTS_COUNT = int(os.environ["POINTS_COUNT"])


def main():
    client = connect()
    migrate(client)
    # points(client)
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
    for batch_number in range(batch_count):
        pts = batch(batch_number, BATCH_SIZE, POINTS_COUNT)
        load(client, pts)

def close(client):
    client.close()


def batch(batch_number: int, batch_size: int, num_pts: int):
    indices = arange(batch_number * batch_size, (batch_number + 1) * batch_size, dtype=float) + 0.5
    phi = arccos(1 - 2 * indices / num_pts)
    theta = pi * (1 + 5 ** 0.5) * indices % (2 * pi)
    x, y, z = cos(theta) * sin(phi), sin(theta) * sin(phi), cos(phi)
    lat, lng = arcsin(z / sqrt(x * x + y * y + z * z)), arccos(x / sqrt(x * x + y * y))
    return lat, lng
    # print(max(lat))
    # print(min(lat))
    # print(max(lon))
    # print(min(lon))
    # pp.figure(figsize=(16, 12)).add_subplot(111, projection='3d').scatter(x, y, z)
    # pp.xlabel('X')
    # pp.ylabel('Y')
    # pp.show()


def load(client, points):
    pass


if __name__ == '__main__':
    main()
