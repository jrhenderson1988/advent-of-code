class Grid:
    def __init__(self, serial_number, grid_size=300):
        self.grid_size = grid_size
        self.serial_number = serial_number
        self.power_levels = {}

    def calculate_power_level(self, x, y):
        rack_id = x + 10
        power_level = rack_id * y
        power_level += self.serial_number
        power_level *= rack_id
        power_level = self.get_hundreds_digit(power_level)
        power_level -= 5

        return power_level

    @staticmethod
    def get_hundreds_digit(number):
        if number < 100:
            return 0

        return int(str(number)[-3])

    @staticmethod
    def get_key(x, y):
        return '%d,%d' % (x, y)

    def calculate_all_power_levels(self):
        if len(self.power_levels) == 0:
            for x in range(1, self.grid_size + 1):
                for y in range(1, self.grid_size + 1):
                    self.power_levels[self.get_key(x, y)] = self.calculate_power_level(x, y)

    def largest_power_grid(self, grid_size):
        self.calculate_all_power_levels()

        largest = None
        for x in range(1, self.grid_size - (grid_size - 2)):
            for y in range(1, self.grid_size - (grid_size - 2)):
                total = 0
                for i in range(grid_size):
                    for j in range(grid_size):
                        total += self.power_levels[self.get_key(x + i, y + j)]

                if largest is None or total > largest[2]:
                    largest = (x, y, total)

        return largest

    def largest_power_square(self):
        self.calculate_all_power_levels()

        largest = None
        for s in range(1, self.grid_size + 1):
            square = self.largest_power_grid(s)
            print('Done grid %d' % s)
            if largest is None or square[2] > largest[3]:
                largest = (square[0], square[1], s, square[2])
                print('--- New largest: %d,%d,%d: %d' % largest)
