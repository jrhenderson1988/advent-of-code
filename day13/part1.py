from loader import load_input_as_string
import os


class Track:
    HORIZONTAL = 1
    VERTICAL = 2
    CORNER_A = 3
    CORNER_B = 4
    CROSSROAD = 5

    DIRECTION_MAP = {
        '-': HORIZONTAL,
        '|': VERTICAL,
        '/': CORNER_A,
        '\\': CORNER_B,
        '+': CROSSROAD
    }

    def __init__(self, track, carts):
        self.track = track
        self.carts = carts

    def __repr__(self):
        s = ''

        carts = {}
        for cart in self.carts:
            carts[cart.y][cart.x] = str(cart)

        for y in range(len(self.track)):
            for x in range(len(self.track[y])):
                if y in carts and x in carts[y]:
                    s += carts[y][x]
                s += self.track[y][x] if self.track[y][x] is not None else ' '
            s += '\n'

        return s

    @staticmethod
    def parse(data: str):
        rows = data.split('\n')
        row_length = max(len(row) for row in rows)

        carts = []
        track = [[None] * row_length for _ in range(len(rows))]
        for y in range(len(rows)):
            length = len(rows[y])
            for x in range(row_length):
                cell = None if x >= length or rows[y][x] is None else rows[y][x]
                if Cart.is_cart(cell):
                    cart = Cart.parse(x, y, cell)
                    carts.append(cart)
                    cell = '-' if cart.is_horizontal() else '|'

                track[y][x] = cell

        return Track(track, carts)

    def is_horizontal(self, x, y):
        return self.track[y][x] == '-'

    def is_vertical(self, x, y):
        return self.track[y][x] == '|'

    def is_crossroad(self, x, y):
        return self.track[y][x] == '+'

    def is_corner(self, x, y):
        return self.track[y][x] == '/' or self.track[y][x] == '\\'

    @staticmethod
    def parse_cell(cell):
        return None if cell == ' ' else cell


class Cart:
    UP = 1
    DOWN = 2
    RIGHT = 3
    LEFT = 4

    DIRECTION_MAP = {
        '^': UP,
        'v': DOWN,
        '>': RIGHT,
        '<': LEFT
    }

    def __init__(self, x: int, y: int, direction: int):
        self.x = x
        self.y = y
        self.direction = direction

    def __repr__(self):
        return

    def is_left(self):
        return self.direction == Cart.LEFT

    def is_right(self):
        return self.direction == Cart.RIGHT

    def is_up(self):
        return self.direction == Cart.UP

    def is_down(self):
        return self.direction == Cart.DOWN

    def is_horizontal(self):
        return self.is_left() or self.is_right()

    def is_vertical(self):
        return self.is_up() or self.is_down()

    @staticmethod
    def parse(x, y, d):
        return Cart(x, y, Cart.DIRECTION_MAP[d])

    @staticmethod
    def is_cart(c):
        return c in Cart.DIRECTION_MAP


def run():
    track = Track.parse(load_input_as_string(os.path.join(os.path.dirname(__file__), 'input.txt')))
    print(track)
    return 'Day 13 Part 1'
