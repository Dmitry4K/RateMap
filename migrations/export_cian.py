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
    "only_flat": {{
        "type": "term",
        "value": "true"
    }},
    "extended": true,
    "subdomain": "www"
}}'''


def get_body(top_left, bottom_right, price, area):
    return request_body.format(bottom_right[0], bottom_right[1], top_left[0], top_left[1], price[1], price[0], area[1],
                               area[0])


def try_request(top_left, bottom_right, price, area):
    return requests.post(
        url=url,
        data=get_body(top_left, bottom_right, price, area)
    )


def save_response(response, area, file):
    for row in response.json()['filtered']:
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
                'avgPriceForMeter': ((row['minPrice'] + row['maxPrice']) / 2) / ((area[1] + area[0]) / 2),
                'exportTime': datetime.datetime.now().isoformat()
            }
            json.dump(result, file)
            file.write(',\n')
            print(result)
        except Exception as e:
            print(e)


def request(top_left, bottom_right, file, price, area):
    attempt = 0
    while True:
        attempt += 1
        try:
            response = try_request(top_left, bottom_right, price, area)
            if response:
                save_response(response, area, file)
                time.sleep(1)
                print(f"Successfully for:", top_left, bottom_right, price, area)
                break
            print(f"ERROR on attempt: {attempt} with error reason: {response.reason} for:", top_left, bottom_right,
                  price,
                  area)
        except Exception as e:
            print(f"ERROR on attempt: {attempt} with exception: {e} for:", top_left, bottom_right, price,
                  area)

        sleep_time_secs = 60 * attempt
        print(f"Sleeping {sleep_time_secs} secs")
        time.sleep(sleep_time_secs)


def create_file(area_range):
    top_left = (55.92457508047347, 37.347976435343874)
    bottom_right = (55.571756200146616, 37.83000158182825)
    price_range = (5_000_000, 50_000_000)
    price_step = 5_000_000
    # area_range = (90, 110)
    # area_range = (20, 50)
    # area_range = (50, 80)

    area_step = 10
    price_count = (price_range[1] - price_range[0]) // price_step + 1
    area_count = (area_range[1] - area_range[0]) // area_step
    grid_size = 7
    step_left = (top_left[0] - bottom_right[0]) / grid_size
    step_bottom = abs((top_left[1] - bottom_right[1]) / grid_size)
    iterations_count = grid_size * grid_size * price_count * area_count
    iteration = 0
    with open(f"cian_export_{area_range[0]}_{area_range[1]}.json", 'w') as file:
        file.write('[\n')
        for i in range(grid_size):
            for j in range(grid_size):
                for p in range(price_count):
                    for a in range(area_count):
                        iteration += 1
                        int_top_left = (top_left[0] - step_left * i, top_left[1] + step_bottom * j)
                        int_bottom_right = (top_left[0] - step_left * (i + 1), top_left[1] + step_bottom * (j + 1))
                        price_lte = 999_999_999 if (p == price_count - 1) else price_range[0] + price_step * (p + 1)
                        price_gte = price_range[0] + price_step * p
                        price = (price_gte, price_lte)
                        area = (area_range[0] + area_step * a, area_range[0] + area_step * (a + 1))
                        print(f"Progress {iteration * 100 // iterations_count}% ({iteration} / {iterations_count})")
                        request(int_top_left, int_bottom_right, file, price, area)

        file.write(']\n')


def main():
    #create_file((20, 30))
    #create_file((30, 40))
    create_file((40, 50))
    create_file((50, 60))
    create_file((60, 70))
    create_file((70, 80))
    create_file((80, 90))
    create_file((90, 100))
    create_file((100, 110))


main()
