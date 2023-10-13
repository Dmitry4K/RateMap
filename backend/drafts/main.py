class GeoRate:

    def __init__(self, rate, weight):
        self.rate = rate
        self.weight = weight


def main():
    exp = 4
    rates = [GeoRate(1, 0.002), GeoRate(1, 1), GeoRate(1, 0.001)]
    rate = 10
    weight = sum([x.weight for x in rates]) / len(rates)
    new_rate = (sum([x.rate * pow(x.weight, exp) for x in rates]) + rate * pow(weight, exp)) / \
               (sum([pow(x.weight, exp) for x in rates]) + pow(weight, exp))
    print(new_rate, weight)


main()
