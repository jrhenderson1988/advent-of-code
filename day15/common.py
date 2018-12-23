class Point:
    def __init__(self, x: int, y: int):
        self.x = x
        self.y = y

    def __repr__(self):
        return '(%d, %d)' % (self.x, self.y)

    def __hash__(self):
        return hash(str(self))

    def __eq__(self, other):
        return hasattr(other, 'x') and hasattr(other, 'y') and other.x == self.x and other.y == self.y


class Distance:
    def __init__(self, distance, prev_node):
        self.distance = distance
        self.prev_node = prev_node

    def __repr__(self):
        return '%d -> %s' % (self.distance, self.prev_node)


class Entity:
    pass


class Square(Entity):
    def __repr__(self):
        return '.'


class Unit(Entity):
    ELF = 1
    GOBLIN = 2
    SYMBOL_MAP = {
        'E': ELF,
        'G': GOBLIN
    }

    def __init__(self, race: int, hp: int, ap: int):
        self.race = race
        self.hp = hp
        self.ap = ap

    def __repr__(self):
        return '%s (%d/%d)' % (self.get_symbol(), self.hp, self.ap)

    def get_symbol(self):
        return Unit.race_to_symbol(self.race)

    @staticmethod
    def parse(symbol: str, hp: int, ap: int):
        return Unit(Unit.symbol_to_race(symbol), hp, ap)

    @staticmethod
    def is_unit_symbol(symbol):
        return symbol in Unit.SYMBOL_MAP

    @staticmethod
    def symbol_to_race(symbol):
        if Unit.is_unit_symbol(symbol):
            return Unit.SYMBOL_MAP[symbol]

        raise ValueError('Unrecognised unit symbol %s' % symbol)

    @staticmethod
    def race_to_symbol(marker):
        for symbol, m in Unit.SYMBOL_MAP.items():
            if marker == m:
                return symbol

        raise ValueError('Unrecognised unit marker %s' % marker)


class Cave:
    def __init__(self, area: dict):
        self.area = area

    def __repr__(self):
        s = ''

        limit = Point(max([p.x for p in self.area.keys()]) + 1, max([p.y for p in self.area.keys()]) + 1)
        for y in range(0, limit.y + 1):
            for x in range(0, limit.x + 1):
                p = Point(x, y)
                if p in self.area:
                    s += self.area[p].get_symbol() if isinstance(self.area[p], Unit) else str(self.area[p])
                else:
                    s += '#'
            s += '\n'

        return s

    def get_units(self):
        return {point: item for point, item in self.area.items() if isinstance(item, Unit)}

    def get_open_neighbours(self, p: Point):
        return [pt for pt in self.get_neighbours(p) if pt in self.area and isinstance(self.area[pt], Square)]

    def dijkstra(self, point_from: Point, point_to: Point):
        visited = []
        queue = [point_from]
        current = point_from
        distances = {current: Distance(0, None)}

        while len(queue) > 0:
            for n in [n for n in self.get_open_neighbours(current) if n not in visited]:
                queue.append(n)
                distance = distances[current].distance + 1
                if n not in distances or distance < distances[n].distance:
                    distances[n] = Distance(distance, current)

            queue.remove(current)
            visited.append(current)

            if current == point_to:
                print('Found it bitches')
                distance = distances[current]
                route = [current]
                while distance.prev_node is not None:
                    route.append(distance.prev_node)
                    distance = distances[distance.prev_node]

                route.reverse()

                return route[1:]

            # sorted_queue = sorted(queue, key=lambda p: distances[p].distance)
            # if len(sorted_queue) < 1:
            #     return None
            #
            # current = sorted_queue[0]
            if len(queue) < 1:
                return None

            current = queue[0]

        return None

    @staticmethod
    def get_neighbours(p: Point):
        return [Point(p.x, p.y - 1), Point(p.x - 1, p.y), Point(p.x + 1, p.y), Point(p.x, p.y + 1)]

    @staticmethod
    def parse(data: str):
        lines = data.splitlines()

        area = {
            Point(x, y): Unit.parse(cell, 200, 3) if Unit.is_unit_symbol(cell) else Square()
            for y, line in enumerate(lines)
            for x, cell in enumerate(line)
            if not Cave.is_wall_symbol(cell)
        }

        return Cave(area)

    @staticmethod
    def is_wall_symbol(symbol):
        return symbol == '#'

# class Point:
#     def __init__(self, x: int, y: int):
#         self.x = x
#         self.y = y
#
#     def __repr__(self):
#         return '(%d, %d)' % (self.x, self.y)
#
#
# class Unit:
#     ELF = 1
#     GOBLIN = 2
#
#     SYMBOL_MAP = {
#         'E': ELF,
#         'G': GOBLIN
#     }
#
#     def __init__(self, race, x, y):
#         self.race = race
#         self.x = x
#         self.y = y
#         self.attack_power = 3
#         self.hit_points = 200
#
#     def __repr__(self):
#         return '%s(%d,%d)' % (self.get_symbol(), self.x, self.y)
#
#     def get_symbol(self):
#         return Unit.race_to_symbol(self.race)
#
#     @staticmethod
#     def parse(symbol: str, x: int, y: int):
#         return Unit(Unit.symbol_to_race(symbol), x, y)
#
#     @staticmethod
#     def is_unit_symbol(symbol):
#         return symbol in Unit.SYMBOL_MAP
#
#     @staticmethod
#     def symbol_to_race(symbol):
#         if Unit.is_unit_symbol(symbol):
#             return Unit.SYMBOL_MAP[symbol]
#
#         raise ValueError('Unrecognised unit symbol %s' % symbol)
#
#     @staticmethod
#     def race_to_symbol(marker):
#         for symbol, m in Unit.SYMBOL_MAP.items():
#             if marker == m:
#                 return symbol
#
#         raise ValueError('Unrecognised unit marker %s' % marker)
#
#
# class Zone:
#     OPEN = 1
#     WALL = 2
#
#     SYMBOL_MAP = {
#         '.': OPEN,
#         '#': WALL
#     }
#
#     def __init__(self, zone: list, units: list):
#         self.zone = zone
#         self.units = units
#
#     def __repr__(self):
#         s = ''
#
#         units = {'%d,%d' % (u.x, u.y): u for u in self.units}
#
#         for y in range(len(self.zone)):
#             for x in range(len(self.zone[0])):
#                 key = '%d,%d' % (x, y)
#                 if key in units:
#                     s += str(units[key].get_symbol())
#                 else:
#                     s += Zone.square_to_symbol(self.zone[y][x])
#             s += '\n'
#
#         return s
#
#     def is_open(self, p: Point):
#         return self.zone[p.y][p.x] == Zone.OPEN
#
#     def get_open_neighbours(self, p: Point):
#         neighbours = self.get_neighbours(p)
#
#         return [pt for pt in neighbours if self.is_open(p)]
#
#     @staticmethod
#     def get_neighbours(p: Point):
#         return [Point(p.x, p.y - 1), Point(p.x - 1, p.y), Point(p.x + 1, p.y), Point(p.x, p.y + 1)]
#
#
#     def dijkstra(self, point_from, point_to):
#         # visited = set()
#         # unvisited = set()
#         # distances = {
#         #     point_from: 0
#         # }
#         # current = point_from
#         #
#         # while True:
#         #     unvisited
#
#         return None
#
#     def round(self):
#         # Sort the units into reading order
#         # Run through each unit in the correct order
#         # - Identify all of the enemy targets - If there are no enemy targets, the battle is over.
#         # - If there are enemy targets, their adjacent squares are identified and the distance to each is calculated
#         # -
#         pass
#
#     def get_total_rounds_to_end_battle(self):
#         return 10
#
#     @staticmethod
#     def symbol_to_square(symbol):
#         if symbol in Zone.SYMBOL_MAP:
#             return Zone.SYMBOL_MAP[symbol]
#
#         raise ValueError('Unrecognised zone symbol %s' % symbol)
#
#     @staticmethod
#     def square_to_symbol(marker):
#         for symbol, m in Zone.SYMBOL_MAP.items():
#             if m == marker:
#                 return symbol
#
#         raise ValueError('Unrecognised zone marker %s' % marker)
#
#     @staticmethod
#     def parse(data: str):
#         lines = [line.strip() for line in data.splitlines()]
#         longest = max([len(line) for line in lines])
#
#         units = [
#             Unit.parse(lines[y][x], x, y)
#             for x in range(longest)
#             for y in range(len(lines))
#             if Unit.is_unit_symbol(lines[y][x])
#         ]
#
#         zone = [
#             [
#                 Zone.OPEN if Unit.is_unit_symbol(lines[y][x]) else Zone.symbol_to_square(lines[y][x])
#                 for x in range(longest)
#             ]
#             for y in range(len(lines))
#         ]
#
#         return Zone(zone, units)
