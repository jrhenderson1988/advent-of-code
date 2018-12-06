from .common import load_points
from loader import load_input
import os
import pdb


def distance(x1, y1, x2, y2):
    return abs(x1 - x2) + abs(y1 - y2)


def run():
    points = load_points(load_input(os.path.join(os.path.dirname(__file__), 'input.txt')))

    grid_start = 0
    grid_end = 400

    threshold = 10000
    area = 0
    for x in range(grid_start, grid_end + 1):
        for y in range(grid_start, grid_end + 1):
            total = 0
            for p in points.values():
                d = distance(x, y, p.x, p.y)
                total += d

            if total < threshold:
                area += 1

    return area