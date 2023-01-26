from .common import load_points


def distance(x1, y1, x2, y2):
    return abs(x1 - x2) + abs(y1 - y2)


def run(content, threshold=10000):
    points = load_points(content.strip().splitlines())

    grid_start = 0
    grid_end = 400

    area = 0
    for x in range(grid_start, grid_end + 1):
        for y in range(grid_start, grid_end + 1):
            total = 0
            for p in points.values():
                d = distance(x, y, p.x, p.y)
                total += d

            if total < threshold:
                area += 1

    return area
