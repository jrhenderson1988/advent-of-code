class Grid:
    def __init__(self, serial_number, grid_size=300):
        self.size = grid_size
        self.serial_number = serial_number
        self.grid = {}
        self.sat = {}

    def power_level(self, x, y):
        rack_id = x + 10

        return self.get_hundreds_digit(((rack_id * y) + self.serial_number) * rack_id) - 5

    @staticmethod
    def summed_area(self, x, y):
        return self.grid[x][y] + self.grid[x][y - 1] + self.grid[x - 1][y] - self.grid[x - 1][y - 1]

    @staticmethod
    def get_hundreds_digit(number):
        if number < 100:
            return 0

        return int(str(number)[-3])

    @staticmethod
    def get_key(x, y):
        return '%d,%d' % (x, y)

    def compute_grid(self):
        self.grid = {x: {y: self.power_level(x, y) for y in range(1, self.size + 1)} for x in range(1, self.size + 1)}
        self.sat = {x: {y: self.summed_area(x, y) for y in range(2, self.size)} for x in range(2, self.size)}

    def largest_power_grid(self, grid_size):
        self.compute_grid()

        largest = None
        for x in range(1, self.size - (grid_size - 2)):
            for y in range(1, self.size - (grid_size - 2)):
                total = 0
                for i in range(grid_size):
                    for j in range(grid_size):
                        total += self.grid[x + i][y + j]

                if largest is None or total > largest[2]:
                    largest = (x, y, total)

        return largest

    def largest_power_square(self):
        self.compute_grid()

        largest = None
        for s in range(1, self.size + 1):
            square = self.largest_power_grid(s)
            print('Done grid %d' % s)
            if largest is None or square[2] > largest[3]:
                largest = (square[0], square[1], s, square[2])
                print('--- New largest: %d,%d,%d: %d' % largest)
