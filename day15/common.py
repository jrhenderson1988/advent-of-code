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
        self.units = {str(uuid.uuid4()): u for u in units}
        self.battle_over = False

    def __repr__(self):
        s = ''

        units = {u.point: u for u in self.living_units().values()}
        for y in range(max([point.y for point in self.zone]) + 2):
            hit_points = []
            for x in range(max([point.x for point in self.zone]) + 2):
                p = Point(x, y)
                s += units[p].get_symbol() if p in units else '.' if p in self.zone else '#'
                if p in units:
                    hit_points.append(units[p].hp)
            s += '   ' + ', '.join([str(hp) for hp in hit_points]) + '\n'

        return s

    def living_units(self):
        return {k: u for k, u in self.units.items() if u.is_alive()}

    def is_open(self, p: Point):
        return p in self.zone and p not in [u.point for u in self.living_units().values()]

    def get_open_neighbours(self, p: Point):
        return [pt for pt in self.get_adjacent_points(p) if self.is_open(pt)]

    @staticmethod
    def get_adjacent_points(p: Point):
        return [Point(p.x, p.y - 1), Point(p.x - 1, p.y), Point(p.x + 1, p.y), Point(p.x, p.y + 1)]

    def get_enemies_of(self, unit: Unit):
        return {k: u for k, u in self.living_units().items() if unit.is_enemy(u)}

    def find_next_move(self, unit: Unit):
        enemies = self.get_enemies_of(unit)
        targets = [p for e in enemies.values() for p in self.get_adjacent_points(e.point)]
        if unit.point in targets:
            return None

        targets = sorted([t for t in targets if self.is_open(t)])
        frontier = deque([(neighbour, unit.point) for neighbour in self.get_open_neighbours(unit.point)])
        visited = {unit.point: None}
        while len(frontier) > 0:
            queue = frontier.copy()
            frontier.clear()
            possible_paths = []

            while len(queue) > 0:
                point, prev = queue.popleft()
                if point in targets:
                    path = [point]
                    while prev is not None:
                        if visited[prev] is not None:
                            path.append(prev)
                        prev = visited[prev]

                    if len(path) > 0:
                        path.reverse()
                        possible_paths.append(path)
                elif point not in visited:
                    visited[point] = prev
                    frontier.extend([
                        (neighbour, point)
                        for neighbour in self.get_open_neighbours(point)
                        if neighbour not in visited
                    ])

            if len(possible_paths) > 0:
                shortest_path_length = min([len(path) for path in possible_paths])
                possible_paths = [path for path in possible_paths if len(path) == shortest_path_length]

                if unit.point == Point(7, 4):
                    print(unit, possible_paths)

                best_target = sorted([path[-1] for path in possible_paths])[0]
                possible_paths = [path for path in possible_paths if path[-1] == best_target]

                best = sorted(possible_paths, key=lambda p: p[0])[0][0]

                return best

        return None

    def select_target(self, unit: Unit):
        enemies = self.get_enemies_of(unit)
        nearby_enemies = {k: u for k, u in enemies.items() if u.point in self.get_adjacent_points(unit.point)}
        if len(nearby_enemies) > 0:
            return sorted(nearby_enemies.keys(), key=lambda k: (nearby_enemies[k].hp, nearby_enemies[k].point))[0]

    def round(self):
        living_units = self.living_units()
        for key in sorted(living_units.keys(), key=lambda k: living_units[k].point):
            if not self.units[key].is_alive():
                continue

            if len(self.get_enemies_of(self.units[key])) == 0:
                self.battle_over = True
                return False

            next_move = self.find_next_move(self.units[key])
            if next_move is not None:
                self.units[key].point = next_move

            target_key = self.select_target(self.units[key])
            if target_key is not None:
                self.units[target_key].hp -= self.units[key].ap

        return True

    def get_outcome(self):
        rounds = 0
        while not self.battle_over:
            if self.round():
                rounds += 1

        return sum(unit.hp for unit in self.living_units().values()) * rounds

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