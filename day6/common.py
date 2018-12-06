import os
from loader import load_input

def load_points(input: list):
    points = {}
    i = 0
    for line in input:
        x, y = [int(s.strip()) for s in line.split(',')]
        points[i] = Point(x, y)
        i += 1

    return points


class Point:
    def __init__(self, x: int, y: int):
        self.x = x
        self.y = y

    def __repr__(self):
        return '(%d, %d)' % (self.x, self.y)

    def __str__(self):
        return self.__repr__()


class Grid:
    def __init__(self, top_left: Point, bottom_right: Point, points: dict):
        self.top_left = top_left
        self.bottom_right = bottom_right
        self.points = points
        self.areas = {}
        self.grid = {}
        self.plotted = False

    def plot(self):
        if self.plotted:
            return

        for y in range(self.top_left.x, self.bottom_right.x + 1):
            for x in range(self.top_left.y, self.bottom_right.y + 1):
                closest = self.closest_point_with_distance(Point(x, y))
                if closest is None:
                    self.remove(x, y)
                else:
                    closest_point, distance = closest
                    self.set(x, y, closest_point, distance)
                    self.increment_area(closest_point)

        self.plotted = True

    def increment_area(self, point):
        if point not in self.areas:
            self.areas[point] = 0

        self.areas[point] += 1

    def set(self, x: int, y: int, closest: str, distance: int):
        if x not in self.grid:
            self.grid[x] = {}

        self.grid[x][y] = (closest, distance)

    def remove(self, x: int, y: int):
        if x not in self.grid:
            self.grid[x] = {}
        
        self.grid[x][y] = None

    def get(self, x: int, y: int):
        if x not in self.grid or y not in self.grid[x]:
            return None

        return self.grid[x][y]

    def distance(self, p1: Point, p2: Point):
        return abs(p1.x - p2.x) + abs(p1.y - p2.y)

    def closest_point_with_distance(self, point: Point):
        distances = {i: self.distance(point, p) for i, p in self.points.items()}
        closest_points = []
        shortest_distance = None

        for i, d in distances.items():
            if shortest_distance is None or d < shortest_distance:
                closest_points = [i]
                shortest_distance = d
            elif d == shortest_distance:
                closest_points.append(i)

        return (closest_points[0], shortest_distance) if len(closest_points) == 1 else None

    def __repr__(self):
        self.plot()

        s = 'Grid [%d,%d -> %d,%d]\n' % (self.top_left.x, self.top_left.y, self.bottom_right.x, self.bottom_right.y)
        for y in range(self.top_left.y, self.bottom_right.y):
            for x in range(self.top_left.x, self.bottom_right.x):
                value = self.grid[x][y]
                s += '.' if value is None else str(value[0])
            s += '\n'
        return s

    def __str__(self):
        return self.__repr__()
