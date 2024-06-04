import json
import requests
import matplotlib.pyplot as plt
import numpy as np
from matplotlib.ticker import FormatStrFormatter


def save_file(filename):
    with open(filename, "r") as f:
        content = f.read()
        if not content:
            print("Error while reading")
            return
        rows = json.loads(content)
        res = list()
        for i, row in enumerate(rows):
            for j in range(row["count"]):
                res.append(row["avgPriceForMeter"])
    return res


def main():
    a = save_file("cian_export_20_30.json")
    b = save_file("cian_export_30_40.json")
    c = save_file("cian_export_40_50.json")
    d = save_file("cian_export_50_60.json")
    e = save_file("cian_export_60_70.json")
    f = save_file("cian_export_70_80.json")
    g = save_file("cian_export_80_90.json")
    h = save_file("cian_export_90_100.json")
    w = save_file("cian_export_100_110.json")
    l = [*a, *b, *c, *d, *e, *f, *g, *h, *w]
    l.sort()
    array = np.array(l)

    fig, ax = plt.subplots(figsize=(8, 4), tight_layout=True)

    ax.hist(array, bins=np.linspace(0, 2000000, 100))
    ax.tick_params(axis='x', rotation=20)
    ax.xaxis.set_major_formatter(FormatStrFormatter('%.0f'))
    #plt.title("Статистка данных объвлений ЦИАН")
    plt.xlabel("Cтоимость 1-ого квадратного метра, р")
    plt.ylabel("Количество объявлений")
    # plt.rcParams.update({'font.size': 52})
    plt.show()
    print(array)


main()
