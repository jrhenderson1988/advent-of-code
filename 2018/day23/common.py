import re


class Nanobot:
    def __init__(self, x, y, z, r):
        self.x = x
        self.y = y
        self.z = z
        self.r = r

    @staticmethod
    def parse(line):
        rgx = re.search(r'^pos=<(-?\d+),(-?\d+),(-?\d+)>, r=(\d+)$', line)
        if not rgx:
            raise Exception("invalid line: %s" % line)

        x, y, z, r = [int(v) for v in rgx.groups()]
        return Nanobot(x, y, z, r)

    def __repr__(self):
        return "Nanobot(x=%d, y=%d, z=%d, r=%d)" % (self.x, self.y, self.z, self.r)

    def in_range(self, other):
        return self.distance_to(other) <= self.r

    def distance_to(self, other):
        return abs(self.x - other.x) + abs(self.y - other.y) + abs(self.z - other.z)

    def strength(self, others):
        return len(list(filter(lambda other: self.in_range(other), others))) + 1

