from common import load_input
from .Grid import Grid
from .Claim import Claim
import os


def run():
    grid = Grid(1000, 1000)
    for claim in load_input(os.path.join(os.path.dirname(__file__), 'input.txt')):
        grid.plot_claim(Claim.parse(claim))

    return grid.total_overlaps()
