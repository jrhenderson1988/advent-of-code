from .Grid import Grid
from .Claim import Claim


def run(content):
    grid = Grid(1000, 1000)
    for claim in content.strip().splitlines():
        grid.plot_claim(Claim.parse(claim))

    return grid.total_overlaps()
