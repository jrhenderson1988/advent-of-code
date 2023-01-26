from .Claim import Claim
from .Grid import Grid


def run(content):
    grid = Grid(1000, 1000)
    for claim in content.strip().splitlines():
        grid.plot_claim(Claim.parse(claim))

    return grid.unique_claims().pop()
