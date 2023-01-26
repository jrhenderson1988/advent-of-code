from .common import Grid


def run(content):
    serial_number = int(content)

    grid = Grid(serial_number)

    x, y, _ = grid.largest_power_grid(3)
    return '%d,%d' % (x, y)
