from .common import Zone


def run(content):
    zone = Zone.parse(content.strip())

    return zone.part1()
