import json
import requests

def save_file(filename, max_value, min_value):
    with open(filename, "r") as f:
        content = f.read()
        if not content:
            print("Error while reading")
            return
        rows = json.loads(content)
        for i, row in enumerate(rows):
            body = {
                'point': {
                    'lat': row["lat"],
                    'lng': row["lng"]
                },
                'avgMeterPrice': {
                    'value': row["avgPriceForMeter"],
                    'count': row["count"]
                }
            }
            min_value = min(min_value, row["avgPriceForMeter"])
            max_value = max(max_value, row["avgPriceForMeter"])

            while True:
                response = requests.post(url="http://localhost:8080/api/v1/layer/avgMetersPrice/set", data=json.dumps(body),
                                         headers={'Content-Type': 'application/json'})
                if response:
                    break
            print(f"Done {i} of {len(rows)}")
    return max_value, min_value

def main():
    max_value, min_value = save_file("cian_export_20_30.json", 0, 100000000000)
    print(max_value, min_value)
    max_value, min_value = save_file("cian_export_30_40.json", max_value, min_value)
    print(max_value, min_value)
    max_value, min_value = save_file("cian_export_40_50.json", max_value, min_value)
    print(max_value, min_value)
    max_value, min_value = save_file("cian_export_50_60.json", max_value, min_value)
    print(max_value, min_value)
    max_value, min_value = save_file("cian_export_60_70.json", max_value, min_value)
    print(max_value, min_value)
    max_value, min_value = save_file("cian_export_70_80.json", max_value, min_value)
    print(max_value, min_value)
    max_value, min_value = save_file("cian_export_80_90.json", max_value, min_value)
    print(max_value, min_value)
    max_value, min_value = save_file("cian_export_90_100.json", max_value, min_value)
    print(max_value, min_value)
    max_value, min_value = save_file("cian_export_100_110.json", max_value, min_value)
    print(max_value, min_value)


main()
