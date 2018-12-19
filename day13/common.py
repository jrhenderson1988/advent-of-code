from operator import attrgetter


class Track:
    HORIZONTAL = 1
    VERTICAL = 2
    CORNER_A = 3
    CORNER_B = 4
    INTERSECTION = 5

    RAIL_MAP = {
        '-': HORIZONTAL,
        '|': VERTICAL,
        '/': CORNER_A,
        '\\': CORNER_B,
        '+': INTERSECTION
    }

    def __init__(self, track, carts):
        self.track = track
        self.carts = carts

    def __repr__(self):
        s = ''

        carts = {}
        for cart in self.carts:
            if cart.y not in carts:
                carts[cart.y] = {}

            carts[cart.y][cart.x] = cart.get_direction_symbol()

        for y in range(len(self.track)):
            for x in range(len(self.track[y])):
                if y in carts and x in carts[y]:
                    s += carts[y][x]
                else:
                    s += Track.direction_to_rail(self.track[y][x]) if self.track[y][x] is not None else ' '
            s += '\n'

        return s

    @staticmethod
    def parse(data: str):
        rows = data.splitlines()
        row_length = max(len(row) for row in rows)

        carts = []
        track = [[None] * row_length for _ in range(len(rows))]
        for y in range(len(rows)):
            length = len(rows[y])
            for x in range(row_length):
                cell = None if x >= length or rows[y][x] is None else rows[y][x]
                if Cart.is_cart_symbol(cell):
                    cart = Cart.parse(len(carts), x, y, cell)
                    cell = Track.direction_to_rail(Track.HORIZONTAL if cart.is_horizontal() else Track.VERTICAL)
                    carts.append(cart)

                track[y][x] = Track.rail_to_direction(cell)

        return Track(track, carts)

    @staticmethod
    def rail_to_direction(symbol):
        return Track.RAIL_MAP[symbol] if symbol in Track.RAIL_MAP else None

    @staticmethod
    def direction_to_rail(direction):
        for rail, d in Track.RAIL_MAP.items():
            if d == direction:
                return rail

        return None

    def is_track(self, x, y):
        return self.track[y][x] is not None

    def is_horizontal(self, x, y):
        return self.track[y][x] == Track.HORIZONTAL

    def is_vertical(self, x, y):
        return self.track[y][x] == Track.VERTICAL

    def is_intersection(self, x, y):
        return self.track[y][x] == Track.INTERSECTION

    def is_corner_a(self, x, y):
        return self.track[y][x] == Track.CORNER_A

    def is_corner_b(self, x, y):
        return self.track[y][x] == Track.CORNER_B

    def is_corner(self, x, y):
        return self.is_corner_a(x, y) or self.is_corner_b(x, y)

    def advance(self):
        carts = sorted(self.carts, key=lambda c: (c.y, c.x))
        collisions = []
        for index, cart in enumerate(carts):
            cart.tick(self)

            other_positions = [(c.x, c.y) for c in carts if c.identifier != cart.identifier]
            if (cart.x, cart.y) in other_positions:
                collisions.append((cart.x, cart.y))

            carts[index] = cart

        self.carts = carts

        return collisions

    def find_first_collision(self):
        while True:
            collisions = self.advance()
            if len(collisions) > 0:
                return collisions[0]


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

    TURN_LEFT = 1
    CONTINUE_STRAIGHT = 2
    TURN_RIGHT = 3

    TURNS = [
        TURN_LEFT,
        CONTINUE_STRAIGHT,
        TURN_RIGHT
    ]

    def __init__(self, identifier: int, x: int, y: int, direction: int):
        self.identifier = identifier
        self.x = x
        self.y = y
        self.state = 0
        self.direction = direction
        self.initial = '%d, %d' % (x, y)

    def __repr__(self):
        return '%s {%d} @ (%s -> %d/%d)' % (self.get_direction_symbol(), self.identifier, self.initial, self.x, self.y)

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

    def get_direction_symbol(self):
        for symbol, direction in Cart.DIRECTION_MAP.items():
            if direction == self.direction:
                return str(symbol)

        return None

    def tick(self, track):
        self.x, self.y = self.get_next_cell()

        if track.is_intersection(self.x, self.y):
            self.turn_at_intersection()

        if track.is_corner(self.x, self.y):
            if track.is_corner_a(self.x, self.y):
                self.turn_at_corner_a()
            else:
                self.turn_at_corner_b()

        return self

    def get_next_cell(self):
        if self.direction == Cart.UP:
            return self.x, self.y - 1
        elif self.direction == Cart.DOWN:
            return self.x, self.y + 1
        elif self.direction == Cart.LEFT:
            return self.x - 1, self.y
        elif self.direction == Cart.RIGHT:
            return self.x + 1, self.y

        raise ValueError('Unknown direction')

    def turn_at_intersection(self):
        turn = Cart.TURNS[self.state]

        if turn == Cart.CONTINUE_STRAIGHT:
            pass
        elif turn == Cart.TURN_LEFT or turn == Cart.TURN_RIGHT:
            if self.direction == Cart.LEFT:
                self.direction = Cart.DOWN if turn == Cart.TURN_LEFT else Cart.UP
            elif self.direction == Cart.UP:
                self.direction = Cart.LEFT if turn == Cart.TURN_LEFT else Cart.RIGHT
            elif self.direction == Cart.RIGHT:
                self.direction = Cart.UP if turn == Cart.TURN_LEFT else Cart.DOWN
            elif self.direction == Cart.DOWN:
                self.direction = Cart.RIGHT if turn == Cart.TURN_LEFT else Cart.LEFT
        else:
            raise ValueError('Invalid turn...')

        self.state = (self.state + 1) % len(Cart.TURNS)

    def turn_at_corner_a(self):
        if self.direction == Cart.LEFT:
            self.direction = Cart.DOWN
        elif self.direction == Cart.UP:
            self.direction = Cart.RIGHT
        elif self.direction == Cart.RIGHT:
            self.direction = Cart.UP
        elif self.direction == Cart.DOWN:
            self.direction = Cart.LEFT

    def turn_at_corner_b(self):
        if self.direction == Cart.LEFT:
            self.direction = Cart.UP
        elif self.direction == Cart.UP:
            self.direction = Cart.LEFT
        elif self.direction == Cart.RIGHT:
            self.direction = Cart.DOWN
        elif self.direction == Cart.DOWN:
            self.direction = Cart.RIGHT

    @staticmethod
    def parse(identifier, x, y, d):
        return Cart(identifier, x, y, Cart.DIRECTION_MAP[d])

    @staticmethod
    def is_cart_symbol(c):
        return c in Cart.DIRECTION_MAP
