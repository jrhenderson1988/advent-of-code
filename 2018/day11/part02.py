from .common import Grid


def run(content):
    serial_number = int(content)

    grid = Grid(serial_number)

    x, y, size, _ = grid.largest_power_square()
    return '%d,%d,%d' % (x, y, size)
