from .common import Grid


def run(content):
    data = content.strip().splitlines()

    grid = Grid.parse(data)

    settled, flowing = grid.run()

    return settled + flowing
