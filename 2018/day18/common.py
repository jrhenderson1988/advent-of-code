import copy


class Area:
    OPEN = 1
    TREE = 2
    LUMBERYARD = 3

    def __init__(self, area: list):
        self.area = area
        self.history = [self.copy_area(self.area)]
        self.max_y = len(self.area)
        self.max_x = len(self.area[0])

    def __repr__(self):
        s = ''
        for y in range(len(self.area)):
            for x in range(len(self.area[y])):
                if self.area[y][x] == Area.OPEN:
                    s += '.'
                elif self.area[y][x] == Area.TREE:
                    s += '|'
                elif self.area[y][x] == Area.LUMBERYARD:
                    s += '#'
                else:
                    s += '?'
            s += '\n'

        return s

    def generate(self):
        new_area = []
        for y in range(self.max_y):
            new_row = []
            for x in range(self.max_x):
                trees, lumberyards = self.get_total_adjacent_trees_and_lumberyards(x, y)
                if self.area[y][x] == Area.OPEN and trees >= 3:
                    new_row.append(Area.TREE)
                elif self.area[y][x] == Area.TREE and lumberyards >= 3:
                    new_row.append(Area.LUMBERYARD)
                elif self.area[y][x] == Area.LUMBERYARD and not (trees >= 1 and lumberyards >= 1):
                    new_row.append(Area.OPEN)
                else:
                    new_row.append(self.area[y][x])
            new_area.append(new_row)

        self.area = new_area

    def get_total_adjacent_trees_and_lumberyards(self, x: int, y: int):
        trees = 0
        lumberyards = 0
        for delta_x, delta_y in [(-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1)]:
            if 0 <= y + delta_y < len(self.area) and 0 <= x + delta_x < len(self.area[y]):
                value = self.area[y + delta_y][x + delta_x]
                if value == Area.TREE:
                    trees += 1
                elif value == Area.LUMBERYARD:
                    lumberyards += 1

        return trees, lumberyards

    @staticmethod
    def copy_area(area: list):
        return copy.deepcopy(area)

    def get_resource_value(self):
        return self.get_total_lumberyards() * self.get_total_trees()

    def get_total_trees(self):
        return len([a for row in self.area for a in row if a == Area.TREE])

    def get_total_lumberyards(self):
        return len([a for row in self.area for a in row if a == Area.LUMBERYARD])

    def get_total_resource_value_after(self, minutes: int):
        for i in range(minutes):
            self.generate()

            if self.area in self.history:
                index = self.history.index(self.area)
                self.history.append(self.copy_area(self.area))
                self.area = self.get_predicted_state_after(minutes, index, i + 1)
                break

            self.history.append(self.copy_area(self.area))

        return self.get_resource_value()

    def get_predicted_state_after(self, minutes: int, index_a: int, index_b: int):
        a = min(index_a, index_b)
        b = max(index_a, index_b)

        diff = b - a
        offset_minutes = minutes - a

        return self.history[a + (offset_minutes % diff)]

    @staticmethod
    def parse(lines: list):
        return Area([[Area.parse_cell(c) for c in line] for line in lines])

    @staticmethod
    def parse_cell(cell):
        if cell == '.':
            return Area.OPEN
        elif cell == '|':
            return Area.TREE
        else:
            return Area.LUMBERYARD
