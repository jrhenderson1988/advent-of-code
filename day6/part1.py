from loader import load_input
from .common import Grid, Point, load_points
import os


def run():
    # Manually specify two grid sizes and compare them to determine which points are fixed and which are infinite.
    # Pretty hacky solution but it works for what I want.
    points = load_points(load_input(os.path.join(os.path.dirname(__file__), 'input.txt')))
    grid = Grid(Point(0, 0), Point(400, 400), points)
    grid2 = Grid(Point(-100, -100), Point(500, 500), points)
    
    grid.plot()
    grid2.plot()

    areas = grid.areas
    areas2 = grid2.areas

    largest = 0
    for i in areas:
        if areas2[i] == areas[i] and areas[i] > largest:
            largest = areas[i]
            
    return largest
