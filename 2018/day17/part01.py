from loader import load_input
from .common import Grid
import os


def run():
    data = load_input(os.path.join(os.path.dirname(__file__), 'input.txt'))

    grid = Grid.parse(data)

    settled, flowing = grid.run()

    return settled + flowing
