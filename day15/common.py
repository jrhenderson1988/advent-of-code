from collections import deque
import uuid


class Point:
    def __init__(self, x: int, y: int):
        self.x = x
        self.y = y

    def __repr__(self):
        return '(%d, %d)' % (self.x, self.y)

    def __hash__(self):
        return hash(str(self))

    def __lt__(self, other):
        return self.y < other.y or (self.y == other.y and self.x < other.x)

    def __le__(self, other):
        return self.__lt__(other) or self.__eq__(other)

    def __eq__(self, other):
        return self.x == other.x and self.y == other.y

    def __ne__(self, other):
        return not self.__eq__(other)

    def __gt__(self, other):
        return self.y > other.y or (self.y == other.y and self.x > other.x)

    def __ge__(self, other):
        return self.__gt__(other) or self.__eq__(other)


class Unit:
    ELF = 1
    GOBLIN = 2
    SYMBOL_MAP = {
        'E': ELF,
        'G': GOBLIN
    }

    def __init__(self, race: int, point: Point):
        self.race = race
        self.point = point
        self.ap = 3
        self.hp = 200

    def __repr__(self):
        return '%s@%d,%d' % (self.get_symbol(), self.point.x, self.point.y)

    def get_symbol(self):
        return Unit.race_to_symbol(self.race)

    def is_enemy(self, other):
        return self.race != other.race

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


class Zone:
    def __init__(self, zone: list, units: list):
        self.zone = zone
        self.units = units
        self.battle_over = False

    def __repr__(self):
        s = ''

        units = {u.point: u.get_symbol() for u in self.units}
        for y in range(max([point.y for point in self.zone]) + 2):
            for x in range(max([point.x for point in self.zone]) + 2):
                p = Point(x, y)
                s += units[p] if p in units else '.' if p in self.zone else '#'
            s += '\n'

        return s

    def is_open(self, p: Point):
        return p in self.zone and p not in [u.point for u in self.units]

    def get_open_neighbours(self, p: Point):
        return [pt for pt in self.get_neighbours(p) if self.is_open(pt)]

    @staticmethod
    def get_neighbours(p: Point):
        return [Point(p.x, p.y - 1), Point(p.x - 1, p.y), Point(p.x + 1, p.y), Point(p.x, p.y + 1)]

    def get_enemies_of(self, unit: Unit):
        return [u for u in self.units if unit.is_enemy(u)]

    def get_nearby_enemies_of(self, unit: Unit):
        return [u for u in self.get_enemies_of(unit) if u.point in self.get_neighbours(unit.point)]

    def find_next_move(self, unit: Unit):
        enemies = self.get_enemies_of(unit)
        targets = [p for e in enemies for p in self.get_neighbours(e.point)]
        if unit.point in targets:
            return None

        targets = sorted([t for t in targets if self.is_open(t)])
        frontier = deque([(neighbour, unit.point) for neighbour in self.get_open_neighbours(unit.point)])
        visited = {unit.point: None}
        while len(frontier) > 0:
            point, prev = frontier.popleft()
            if point in targets:
                route = [point]
                while prev is not None:
                    if visited[prev] is not None:
                        route.append(prev)
                    prev = visited[prev]

                return route[-1] if len(route) > 0 else None

            if point not in visited:
                visited[point] = prev
                frontier.extend([
                    (neighbour, point)
                    for neighbour in self.get_open_neighbours(point)
                    if neighbour not in visited
                ])

        return None

    def attack_nearby_enemies(self, unit: Unit):
        nearby_enemies = self.get_nearby_enemies_of(unit)
        if len(nearby_enemies) > 0:
            most_vulnerable_nearby_enemy = min(nearby_enemies, key=lambda e: e.hp)
            # TODO - Attack the enemy (Re-jig the units so they can be identified with a key to perform updates)
            print(unit, most_vulnerable_nearby_enemy)

        # print(
        #     '%s@%d,%d' % (unit.get_symbol(), unit.point.x, unit.point.y),
        #     ' -> ',
        #     ['%s@%d,%d' % (u.get_symbol(), u.point.x, u.point.y) for u in nearby_enemies]
        # )

        # If there are any enemies located within the given unit's neighbour squares
        # The lowest HP unit is selected to be attacked
        # the target enemy receives a blow, losing HP equivalent to the unit's AP

    def round(self):
        # TODO -> Ensure that we mark the battle as over and return when there are no enemy units left
        self.units = sorted(self.units, key=lambda u: u.point)
        for index, unit in enumerate(self.units):
            next_move = self.find_next_move(unit)
            if next_move is not None:
                self.units[index].point = next_move

            self.attack_nearby_enemies(unit)


        # Sort the units into reading order
        # Run through each unit in the correct order
        # - Identify all of the enemy targets - If there are no enemy targets, the battle is over.
        # - If there are enemy targets, their adjacent squares are identified and the distance to each is calculated
        # -

    def get_outcome(self):
        rounds = 0
        while not self.battle_over:
            self.round()
            rounds += 1

            if rounds > 4:
                self.battle_over = True

            print(self)

        return sum(unit.hp for unit in self.units) * rounds

    @staticmethod
    def parse(data: str):
        lines = [line.strip() for line in data.splitlines()]
        longest = max([len(line) for line in lines])

        units = [
            Unit(Unit.symbol_to_race(lines[y][x]), Point(x, y))
            for x in range(longest)
            for y in range(len(lines))
            if Unit.is_unit_symbol(lines[y][x])
        ]

        unit_points = [u.point for u in units]

        zone = [
            Point(x, y)
            for x in range(longest)
            for y in range(len(lines))
            if lines[y][x] == '.' or Point(x, y) in unit_points
        ]

        return Zone(zone, units)