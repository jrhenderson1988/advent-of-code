class Grid:
    def __init__(self, serial_number, grid_size=300):
        self.size = grid_size
        self.serial_number = serial_number
        self.grid = self.compute_grid()

    def compute_grid(self):
        grid = [[0] * (self.size + 1) for _ in range(self.size + 1)]
        for x in range(1, self.size + 1):
            for y in range(1, self.size + 1):
                grid[x][y] = self.calculate_summed_area_cell(grid, x, y)

        return grid

    def calculate_summed_area_cell(self, grid, x, y):
        if x < 1 or y < 1:
            return 0

        return grid[x - 1][y] + grid[x][y - 1] - grid[x - 1][y - 1] + self.power_level(x, y)

    def power_level(self, x, y):
        rack_id = x + 10
        return self.get_hundreds_digit(((rack_id * y) + self.serial_number) * rack_id) - 5

    def calculate_grid_power(self, x, y, size):
        xr = x + size - 1
        yr = y + size - 1

        return self.grid[xr][yr] - self.grid[xr][y-1] - self.grid[x-1][yr] + self.grid[x-1][y-1]

    @staticmethod
    def get_hundreds_digit(number):
        if number < 100:
            return 0

        return int(str(number)[-3])

    def largest_power_grid(self, grid_size):
        largest = None
        for x in range(1, self.size - (grid_size - 2)):
            for y in range(1, self.size - (grid_size - 2)):
                total = self.calculate_grid_power(x, y, grid_size)
                if largest is None or total > largest[2]:
                    largest = (x, y, total)

        return largest

    def largest_power_square(self):
        largest = None
        for s in range(1, self.size + 1):
            square = self.largest_power_grid(s)
            if largest is None or square[2] > largest[3]:
                largest = (square[0], square[1], s, square[2])

        return largest
