from loader import load_input
from .common import Grid, Point, load_points
import os


def run():
    points = load_points(load_input(os.path.join(os.path.dirname(__file__), 'input.txt')))

    return Grid(points).get_largest_area()
