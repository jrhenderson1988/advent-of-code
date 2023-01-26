from .common import Grid


def run(content):
    data = content.strip().splitlines()

    grid = Grid.parse(data)

    settled, _ = grid.run()

    return settled
