import datetime

import requests
import time
import json

url = "https://api.cian.ru/search-offers-index-map/v1/get-clusters-for-map/"

request_body = '''{{
    "zoom": 15,
    "bbox": [
        {{
            "bottomRight": {{
                "lat": {0},
                "lng": {1}
            }},
            "topLeft": {{
                "lat": {2},
                "lng": {3}
            }}
        }}
    ],
    "jsonQuery": {{
        "_type": "flatsale",
        "engine_version": {{
            "type": "term",
            "value": 1
        }},
        "region": {{
            "type": "terms",
            "value": [
                1
            ]
        }},
        "room": {{
            "type": "terms",
            "value": [
                1,
                2,
                3,
                4,
                5,
                6
            ]
        }},
        "price": {{
            "type": "range",
            "value": {{
                "gte": {5},
                "lte": {4}
            }}
        }},
        "total_area": {{
            "type": "range",
            "value": {{
                "gte": {7},
                "lte": {6}
            }}
        }}
    }},
    "extended": true,
    "subdomain": "www"
}}'''


def get_body(top_left, bottom_right, price, area):
    return request_body.format(bottom_right[0], bottom_right[1], top_left[0], top_left[1], price[1], price[0], area[1], area[0])


def request(top_left, bottom_right, file, price, area):
    response = requests.post(
        url=url,
        data=get_body(top_left, bottom_right, price, area)
    )
    if response:
        resp = response.json()
        for row in resp['filtered']:
            try:
                result = {
                    'lat': row['coordinates']['lat'],
                    'lng': row['coordinates']['lng'],
                    'minPrice': row['minPrice'],
                    'maxPrice': row['maxPrice'],
                    'count': row['count'],
                    'avgPrice': (row['minPrice'] + row['maxPrice']) / 2,
                    'minArea': area[0],
                    'maxArea': area[1],
                    'avgArea': (area[1] + area[0]) / 2,
                    'exportTime': datetime.datetime.now().isoformat()
                }
                json.dump(result, file)
                file.write(',\n')
                print(result)
            except Exception as e:
                print(e)
        print(f"Successfully for:", top_left, bottom_right, price, area)
        time.sleep(7)
    else:
        print(f"ERROR for {response.reason}")


def main():
    top_left = (55.92457508047347, 37.347976435343874)
    bottom_right = (55.571756200146616, 37.83000158182825)
    price_range = (5_000_000, 50_000_000)
    price_step = 5_000_000
    area_range = (20, 120)
    area_step = 10
    price_count = (price_range[1] - price_range[0]) // price_step + 1
    area_count = (area_range[1] - area_range[0]) // area_step
    grid_size = 7
    step_left = (top_left[0] - bottom_right[0]) / grid_size
    step_bottom = abs((top_left[1] - bottom_right[1]) / grid_size)
    with open("cian_export.json", 'w') as file:
        file.write('[\n')
        for i in range(grid_size):
            for j in range(grid_size):
                for p in range(price_count):
                    for a in range(area_count):
                        int_top_left = (top_left[0] - step_left * i, top_left[1] + step_bottom * j)
                        int_bottom_right = (top_left[0] - step_left * (i + 1), top_left[1] + step_bottom * (j + 1))
                        price_lte = 999_999_999 if (p == price_count - 1) else price_range[0] + price_step * (p + 1)
                        price_gte = price_range[0] + price_step * p
                        price = (price_gte, price_lte)
                        area = (area_range[0] + area_step * a, area_range[0] + area_step * (a + 1))
                        # print(area)
                        request(int_top_left, int_bottom_right, file, price, area)

        file.write(']\n')


main()
