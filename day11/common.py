class Grid:
    def __init__(self, serial_number, grid_size=300):
        self.size = grid_size
        self.serial_number = serial_number
        self.grid = self.compute_grid()

    def compute_grid(self):
        grid = [[0] * (self.size + 1) for _ in range(self.size + 1)]
        for x in range(1, self.size + 1):
            for y in range(1, self.size + 1):
                print(x, y, self.calculate_summed_area_cell(grid, x, y))
                grid[x][y] = self.calculate_summed_area_cell(grid, x, y)

        return grid

    def calculate_summed_area_cell(self, grid, x, y):
        if x < 1 or y < 1:
            return 0

        return grid[x - 1][y] + grid[x][y - 1] - grid[x - 1][y - 1] + self.power_level(x, y)

    def power_level(self, x, y):
        rack_id = x + 10
        return self.get_hundreds_digit(((rack_id * y) + self.serial_number) * rack_id) - 5

    # def summed_area(self, x, y):
    #     return self.grid[x][y] + \
    #            self.grid[x][y - 1] + \
    #            self.grid[x - 1][y] - \
    #            self.grid[x - 1][y - 1]
    #
    # def calculate_area(self, x, y, s):
    #     return self.summed_area_table[x][y] + \
    #            self.summed_area_table[x + s][y + s] - \
    #            self.summed_area_table[x + s][y] - \
    #            self.summed_area_table[x][y + s]

    @staticmethod
    def get_hundreds_digit(number):
        if number < 100:
            return 0

        return int(str(number)[-3])

    # @staticmethod
    # def get_key(x, y):
    #     return '%d,%d' % (x, y)

    # def compute_grid(self):
    #     s = self.size
    #     self.grid = {x: {y: self.power_level(x, y) for y in range(1, s + 1)} for x in range(1, s + 1)}
    #     # self.summed_area_table = {x: {y: self.summed_area(x, y) for y in range(0, s)} for x in range(0, s)}
    #     # print(self.summed_area_table)
    #
    #     def summed_area(x, y):
    #         if x < 1 or y < 1:
    #             return 0
    #         else:
    #
    #     self.summed_area_table = {}
    #     for y in range(0, self.size + 1):
    #         for x in range(0, self.size + 1):
    #             if x == 0 or y == 0:
    #                 self.summed_area_table[y][x] = 0
    #             else:
    #                 self.summed_area_table[y][x] = self.summed_area_table[y-1][x] + self.summed_area_table[y][x-1] - self.summed_area_table[y-1][x-1] + self.power_level(x, y)

    def largest_power_grid(self, grid_size):
        self.compute_grid()

        largest = None
        for x in range(1, self.size - (grid_size - 2)):
            for y in range(1, self.size - (grid_size - 2)):
                total = self.calculate_area(x, y, grid_size)
                if largest is None or total > largest[2]:
                    largest = (x, y, total)

        return largest

        # largest = None
        # for x in range(1, self.size - (grid_size - 2)):
        #     for y in range(1, self.size - (grid_size - 2)):
        #         total = 0
        #         for i in range(grid_size):
        #             for j in range(grid_size):
        #                 total += self.grid[x + i][y + j]
        #
        #         if largest is None or total > largest[2]:
        #             largest = (x, y, total)
        #
        # return largest

    def largest_power_square(self):
        self.compute_grid()

        largest = None
        for s in range(1, self.size + 1):
            square = self.largest_power_grid(s)
            print('Done grid %d' % s)
            if largest is None or square[2] > largest[3]:
                largest = (square[0], square[1], s, square[2])
                print('--- New largest: %d,%d,%d: %d' % largest)
