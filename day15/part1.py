from .common import Cave, Point
from loader import load_input_as_string
import os


def run():
    cave = Cave.parse(load_input_as_string(os.path.join(os.path.dirname(__file__), 'input.txt'), trim=False))

    print(cave.dijkstra(Point(8, 3), Point(12, 8)))

    # return cave.get_total_rounds_to_end_battle()
    return 'Day 15 Part 1'
