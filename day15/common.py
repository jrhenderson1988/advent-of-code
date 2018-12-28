from collections import deque
import uuid


class Point:
    def __init__(self, x: int, y: int):
        self.x = x
        self.y = y

    def __repr__(self):
        return '(%d, %d)' % (self.x, self.y)

    def __hash__(self):
        return hash((self.y, self.x))

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

    def is_alive(self):
        return self.hp > 0

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
        self.initial_units = {str(uuid.uuid4()): u for u in units}
        self.units = self.initial_units
        self.rounds = 0

    def __repr__(self):
        s = ''

        units = {u.point: u for u in self.units.values()}
        for y in range(max([point.y for point in self.zone]) + 2):
            hit_points = []
            for x in range(max([point.x for point in self.zone]) + 2):
                p = Point(x, y)
                s += units[p].get_symbol() if p in units else '.' if p in self.zone else '#'
                if p in units:
                    hit_points.append(units[p].hp)
            s += '   ' + ', '.join([str(hp) for hp in hit_points]) + '\n'

        return s

    def reset(self):
        self.units = self.initial_units
        self.rounds = 0

    def only_open(self, points: list):
        living_unit_positions = [u.point for u in self.units.values()]
        return [p for p in points if p in self.zone and p not in living_unit_positions]

    def get_open_adjacent_points(self, p: Point):
        return self.only_open(self.get_adjacent_points(p))

    @staticmethod
    def get_adjacent_points(p: Point):
        return [Point(p.x, p.y - 1), Point(p.x - 1, p.y), Point(p.x + 1, p.y), Point(p.x, p.y + 1)]

    def get_enemies_of(self, unit: Unit):
        return {k: u for k, u in self.units.items() if unit.is_enemy(u)}

    def find_next_move(self, unit: Unit):
        enemies = self.get_enemies_of(unit)
        targets = [p for e in enemies.values() for p in self.get_adjacent_points(e.point)]
        if unit.point in targets:
            return None

        targets = self.only_open(targets)
        frontier = deque([(neighbour, unit.point) for neighbour in self.get_adjacent_points(unit.point)])
        visited = {unit.point: None}

        open_positions = [p for p in self.zone if p not in [u.point for u in self.units.values()]]

        while len(frontier) > 0:
            point, prev = frontier.popleft()
            if point not in visited:
                if point not in open_positions:
                    continue

                if point in targets:
                    route = [point]
                    while prev is not None:
                        if visited[prev] is not None:
                            route.append(prev)
                        prev = visited[prev]

                    return route[-1] if len(route) > 0 else None

                visited[point] = prev
                frontier.extend([
                    (neighbour, point)
                    for neighbour in self.get_adjacent_points(point)
                    if neighbour not in visited
                ])

        return None

    def select_target(self, unit: Unit):
        enemies = self.get_enemies_of(unit)
        nearby_enemies = {k: u for k, u in enemies.items() if u.point in self.get_adjacent_points(unit.point)}
        if len(nearby_enemies) > 0:
            return sorted(nearby_enemies.keys(), key=lambda k: (nearby_enemies[k].hp, nearby_enemies[k].point))[0]

    def round(self):
        for key in sorted(self.units.keys(), key=lambda k: self.units[k].point):
            if key not in self.units:
                continue

            if len(self.get_enemies_of(self.units[key])) == 0:
                return False

            next_move = self.find_next_move(self.units[key])
            if next_move is not None:
                self.units[key].point = next_move

            target_key = self.select_target(self.units[key])
            if target_key is not None:
                self.units[target_key].hp -= self.units[key].ap
                if self.units[target_key].hp <= 0:
                    del self.units[target_key]

        self.rounds += 1
        return True

    def get_outcome(self):
        while self.round():
            pass

        return sum(unit.hp for unit in self.units.values()) * self.rounds

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
