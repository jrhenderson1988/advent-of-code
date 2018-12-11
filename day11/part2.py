from .common import Grid
from loader import load_input_as_string
import os


def run():
    serial_number = int(load_input_as_string(os.path.join(os.path.dirname(__file__), 'input.txt')))

    grid = Grid(serial_number)

    return grid.largest_power_square()
