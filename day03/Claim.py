from re import compile


class Claim:
    def __init__(self, i, x, y, w, h):
        self.i = int(i)
        self.x = int(x)
        self.y = int(y)
        self.w = int(w)
        self.h = int(h)
        pass

    def __str__(self):
        return '#' + str(self.i) + ' @ ' + str(self.x) + ',' + str(self.y) + ': ' + str(self.w) + 'x' + str(self.h)

    def plot(self, grid):
        return grid

    @staticmethod
    def parse(line):
        pattern = compile(r'^\s*#(?P<i>\d+)\s*@\s*(?P<x>\d+),(?P<y>\d+):\s*(?P<w>\d+)x(?P<h>\d+)\s*$')
        match = pattern.match(line)
        i = int(match.group('i'))
        x = int(match.group('x'))
        y = int(match.group('y'))
        w = int(match.group('w'))
        h = int(match.group('h'))

        return Claim(i, x, y, w, h)
