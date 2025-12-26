from functools import cache


class CaveSystem:
    def __init__(self, depth, target):
        self.depth = depth
        self.target = target

    @staticmethod
    def parse(content: str):
        lines = content.strip().split("\n")
        if not lines[0].startswith("depth:") or not lines[1].startswith("target:"):
            raise Exception("Unexpected input: %s" % content)

        depth = int(lines[0][len("depth:"):].strip())
        target = tuple(map(int, lines[1][len("target:"):].strip().split(",")))
        return CaveSystem(depth, target)

    def risk_level(self):
        return 0

    @cache
    def erosion_level(self, coord):
        return (self.geologic_index(coord) + self.depth) % 20183

    @cache
    def geologic_index(self, coord):
        # The region at 0,0 (the mouth of the cave) has a geologic index of 0.
        if coord == (0, 0):
            return 0
        # The region at the coordinates of the target has a geologic index of 0.
        if coord == self.target:
            return 0
        # If the region's Y coordinate is 0, the geologic index is its X coordinate times 16807.
        if coord[1] == 0:
            return coord[0] * 16807
        # If the region's X coordinate is 0, the geologic index is its Y coordinate times 48271.
        if coord[0] == 0:
            return coord[1] * 48271
        # Otherwise, the region's geologic index is the result of multiplying the erosion levels of the regions at X-1,Y and X,Y-1.
        return self.erosion_level((coord[0] - 1, coord[1])) * self.erosion_level((coord[0], coord[1] - 1))

    def risk_level(self, coord):
        return self.erosion_level(coord) % 3

    def total_risk_factor_for_initial_area(self):
        return sum(self.risk_level((x, y)) for x in range(self.target[0] + 1) for y in range(self.target[1] + 1))
