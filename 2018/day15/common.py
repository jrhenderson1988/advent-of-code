import copy
import uuid
from collections import deque


class Unit:
    ELF = 1
    GOBLIN = 2
    SYMBOL_MAP = {
        'E': ELF,
        'G': GOBLIN
    }

    def __init__(self, race: int, point: tuple):
        self.race = race
        self.point = point
        self.ap = 3
        self.hp = 200

    def __repr__(self):
        return '%s@%d,%d' % (self.get_symbol(), self.point[0], self.point[1])

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
        self.units = self.copy_units(self.initial_units)
        self.rounds = 0
        self.total_units = len(self.units)

    def __repr__(self):
        s = ''

        units = {u.point: u for u in self.units.values()}
        for y in range(max([point[1] for point in self.zone]) + 2):
            hit_points = []
            for x in range(max([point[0] for point in self.zone]) + 2):
                p = (x, y)
                s += units[p].get_symbol() if p in units else '.' if p in self.zone else '#'
                if p in units:
                    hit_points.append(units[p].hp)
            s += '   ' + ', '.join([str(hp) for hp in hit_points]) + '\n'

        return s

    def reset(self):
        self.units = self.copy_units(self.initial_units)
        self.rounds = 0

    def only_open(self, points: list):
        living_unit_positions = [u.point for u in self.units.values()]
        return [p for p in points if p in self.zone and p not in living_unit_positions]

    def get_open_adjacent_points(self, p: tuple):
        return self.only_open(self.get_adjacent_points(p))

    @staticmethod
    def get_adjacent_points(p: tuple):
        return [(p[0], p[1] - 1), (p[0] - 1, p[1]), (p[0] + 1, p[1]), (p[0], p[1] + 1)]

    @staticmethod
    def copy_units(units: dict):
        return copy.deepcopy(units)

    def get_enemies_of(self, unit: Unit):
        return {k: u for k, u in self.units.items() if unit.is_enemy(u)}

    def find_next_move(self, unit: Unit):
        enemies = self.get_enemies_of(unit)
        target_squares = [p for e in enemies.values() for p in self.get_adjacent_points(e.point)]
        if unit.point in target_squares:
            return None

        closest_target = self.find_closest_target(unit.point, target_squares)
        if closest_target is None:
            return None

        return self.find_best_next_step(self.only_open(self.get_adjacent_points(unit.point)), closest_target)

    def find_closest_target(self, source, targets):
        distances = self.shortest_distances_between(source, targets)
        return self.choose_closest(distances)

    def find_best_next_step(self, next_steps, target):
        distances = self.shortest_distances_between(target, next_steps)
        return self.choose_closest(distances)

    def choose_closest(self, distances):
        closest_target = None
        shortest_distance = None
        for p, dist in distances.items():
            if shortest_distance is None or dist < shortest_distance:
                closest_target = p
                shortest_distance = dist
            elif dist == shortest_distance:
                closest_target = self.first_in_reading_order(closest_target, p)

        return closest_target

    def shortest_distances_between(self, source, targets):
        q = deque([(source, 0)])
        explored = {source}
        targets = set(targets)
        distances = {}
        while len(q) > 0 and len(targets) > 0:
            (v, dist) = q.popleft()
            if v in targets:
                targets.remove(v)
                distances[v] = dist
            for w in self.only_open(self.get_adjacent_points(v)):
                if w not in explored:
                    explored.add(w)
                    q.append((w, dist + 1))
        return distances

    def select_target(self, unit: Unit):
        enemies = self.get_enemies_of(unit)
        nearby = {k: u for k, u in enemies.items() if u.point in self.get_adjacent_points(unit.point)}
        if len(nearby) > 0:
            return sorted(nearby.keys(), key=lambda k: (nearby[k].hp, nearby[k].point[1], nearby[k].point[0]))[0]

    def round(self):
        for key in sorted(self.units.keys(), key=lambda k: (self.units[k].point[1], self.units[k].point[0])):
            if key not in self.units:
                continue

            if len(self.get_enemies_of(self.units[key])) == 0:
                # print("round=%d, finished early" % self.rounds)
                return False

            next_move = self.find_next_move(self.units[key])
            if next_move is not None:
                self.units[key].point = next_move

            target_key = self.select_target(self.units[key])
            if target_key is not None:
                self.units[target_key].hp -= self.units[key].ap
                if self.units[target_key].hp <= 0:
                    del self.units[target_key]

        # print("round=%d, completed" % self.rounds)
        self.rounds += 1
        return True

    def get_total_elves(self):
        return len([u for u in self.units.values() if u.race == Unit.ELF])

    def get_total_goblins(self):
        return len([u for u in self.units.values() if u.race == Unit.GOBLIN])

    def get_outcome(self):
        hit_points = sum(unit.hp for unit in self.units.values() if unit.hp > 0)
        rounds = self.rounds
        print('%d HP and %d rounds' % (hit_points, rounds))
        return hit_points * rounds

    @staticmethod
    def first_in_reading_order(a, b):
        if a[1] < b[1]:
            return a
        if b[1] < a[1]:
            return b
        if a[0] < b[0]:
            return a
        if b[0] < a[0]:
            return b
        return a

    def part1(self):
        while self.round():
            pass

        return self.get_outcome()

    def part2(self):
        ap = 4
        initial_elves = len([u for u in self.units.values() if u.race == Unit.ELF])
        while True:
            self.reset()
            for key in self.units.keys():
                if self.units[key].race == Unit.ELF:
                    self.units[key].ap = ap

            while self.round():
                if self.get_total_elves() < initial_elves:
                    break

            if initial_elves == self.get_total_elves():
                return self.get_outcome()

            ap += 1

    @staticmethod
    def parse(data: str):
        lines = [line.strip() for line in data.splitlines()]
        longest = max([len(line) for line in lines])

        units = [
            Unit(Unit.symbol_to_race(lines[y][x]), (x, y))
            for x in range(longest)
            for y in range(len(lines))
            if Unit.is_unit_symbol(lines[y][x])
        ]

        unit_points = [u.point for u in units]

        zone = [
            (x, y)
            for x in range(longest)
            for y in range(len(lines))
            if lines[y][x] == '.' or (x, y) in unit_points
        ]

        return Zone(zone, units)
