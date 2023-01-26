import re


class Star:
    def __init__(self, x: int, y: int, x_velocity: int, y_velocity: int):
        self.x = x
        self.y = y
        self.x_velocity = x_velocity
        self.y_velocity = y_velocity

    def __repr__(self):
        return 'Star (%d,%d [%d,%d])' % (self.x, self.y, self.x_velocity, self.y_velocity)

    @staticmethod
    def parse(line):
        p = re.compile('position=<\s*(?P<px>-?\d+),\s*(?P<py>-?\d+)> velocity=<\s*(?P<vx>-?\d+),\s*(?P<vy>-?\d+)>')
        m = p.match(line)
        if not m:
            raise ValueError('Invalid line: ' + line)

        return Star(int(m.group('px')), int(m.group('py')), int(m.group('vx')), int(m.group('vy')))


class Sky:
    def __init__(self, stars: list):
        self.stars = stars
        self.area = self.calculate_area(stars)
        self.iterations = 0

    def iterate(self):
        new_stars = [Star(s.x + s.x_velocity, s.y + s.y_velocity, s.x_velocity, s.y_velocity) for s in self.stars]
        new_area = self.calculate_area(new_stars)

        if new_area > self.area:
            return False

        self.iterations += 1
        self.stars = new_stars
        self.area = new_area

        return True

    def align_stars(self):
        while self.iterate():
            pass

    def draw_stars(self):
        self.align_stars()

        x = [star.x for star in self.stars]
        y = [star.y for star in self.stars]
        left = min(x)
        right = max(x)
        top = min(y)
        bottom = max(y)

        points = [(s.x, s.y) for s in self.stars]

        s = ''
        for y in range(top, bottom + 1):
            for x in range(left, right + 1):
                if (x, y) in points:
                    s += '#'
                else:
                    s += '.'
            s += '\n'

        return s

    @staticmethod
    def calculate_area(stars: list):
        x = [star.x for star in stars]
        y = [star.y for star in stars]

        return abs(max(x) - min(x)) * abs(max(y) - min(y))
