from .common import Grid, load_points


def run(content):
    points = load_points(content.strip().splitlines())

    return Grid(points).get_largest_area()
