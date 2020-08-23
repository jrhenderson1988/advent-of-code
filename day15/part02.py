from .common import Zone, Unit
from loader import load_input_as_string
import os


def run():
    zone = Zone.parse(load_input_as_string(os.path.join(os.path.dirname(__file__), 'input.txt')))

    return zone.part2()
