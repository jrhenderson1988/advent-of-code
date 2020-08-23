import re


class Grid:
    def __init__(self, clay: list, spring=(500, 0)):
        self.clay = set(clay)
        self.flow = set()
        self.settled = set()
        self.streams = [(spring[0], spring[1])]
        self.spring = spring

    def __repr__(self):
        x_coordinates = [i[0] for i in self.clay] + [self.spring[0]]
        y_coordinates = [i[1] for i in self.clay] + [self.spring[1]]

        s = ''
        for y in range(min(y_coordinates) - 1, max(y_coordinates) + 2):
            for x in range(min(x_coordinates) - 1, max(x_coordinates) + 2):
                xy = (x, y)
                if self.is_clay(xy):
                    s += '#'
                elif self.is_spring(xy):
                    s += '+'
                elif self.is_flow(xy):
                    s += '|'
                elif self.is_settled(xy):
                    s += '~'
                else:
                    s += '.'
            s += '\n'

        return s

    def is_spring(self, xy: tuple):
        return self.spring == xy

    def is_clay(self, xy: tuple):
        return xy in self.clay

    def is_sand(self, xy: tuple):
        return not self.is_clay(xy) and \
               not self.is_spring(xy) and \
               not self.is_flow(xy) and \
               not self.is_settled(xy)

    def is_flow(self, xy: tuple):
        return xy in self.flow

    def is_settled(self, xy: tuple):
        return xy in self.settled

    def is_fillable(self, xy: tuple):
        return self.is_flow(xy) or self.is_sand(xy)

    def run(self):
        max_y = max([xy[1] for xy in self.clay])
        min_y = min([xy[1] for xy in self.clay])
        streams = [(self.spring[0], self.spring[1] + 1)]
        while len(streams) > 0:
            x, y = streams.pop()
            if y > max_y:
                continue

            if self.is_fillable((x, y + 1)):
                self.flow.add((x, y))
                streams.append((x, y + 1))
                continue

            fill_left = x
            while self.is_fillable((fill_left, y)) and not self.is_fillable((fill_left, y + 1)):
                self.flow.add((fill_left, y))
                fill_left -= 1

            fill_right = x
            while self.is_fillable((fill_right, y)) and not self.is_fillable((fill_right, y + 1)):
                self.flow.add((fill_right, y))
                fill_right += 1

            if self.is_clay((fill_left, y)) and self.is_clay((fill_right, y)):
                for j in range(fill_left + 1, fill_right):
                    self.flow.remove((j, y))
                    self.settled.add((j, y))
                streams.append((x, y - 1))
                continue

            if self.is_sand((fill_left, y + 1)):
                streams.append((fill_left, y))

            if self.is_sand((fill_right, y + 1)):
                streams.append((fill_right, y))

        total_flowing = len([xy for xy in self.flow if max_y >= xy[1] >= min_y])
        total_settled = len([xy for xy in self.settled if max_y >= xy[1] >= min_y])

        return total_settled, total_flowing

    @staticmethod
    def parse(lines):
        clay = set()
        for line in lines:
            pattern = re.compile(r'^([xy])=(\d+(?:\.\.\d+)?),\s*([xy])=(\d+(?:\.\.\d+)?)$')
            match = pattern.match(line)
            if match is None:
                raise ValueError('Could not parse line: %s' % line)

            groups = match.groups()
            if groups[0] == groups[2]:
                raise ValueError('Got 2 coordinates of the same axis: %s' % groups[0])

            coords = {groups[0]: groups[1], groups[2]: groups[3]}
            x_coords = [int(c) for c in coords['x'].split('..')]
            y_coords = [int(c) for c in coords['y'].split('..')]
            for x in range(x_coords[0], (x_coords[0] if len(x_coords) == 1 else x_coords[1]) + 1):
                for y in range(y_coords[0], (y_coords[0] if len(y_coords) == 1 else y_coords[1]) + 1):
                    clay.add((x, y))

        return Grid(list(clay))
