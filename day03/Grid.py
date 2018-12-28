from .Claim import Claim


class Grid:
    def __init__(self, x, y):
        self.grid = [[[] for _ in range(0, x)] for _ in range(0, y)]
        self.plotted_claim_ids = []

    def __str__(self):
        o = ''
        for row in self.grid:
            o += '|'
            for cell in row:
                o += str(len(cell)) + '|'
            o += '\n'
        return o
    
    def in_bounds(self, x, y):
        return False if x < 0 or x > (len(self.grid) - 1) or y < 0 or y > (len(self.grid) - 1) else True

    def get(self, x, y):
        if not self.in_bounds(x, y):
            raise ValueError('The coordinates given (' + str(x) + ',' + str(y) + ') are out of bounds')

        return self.grid[x][y]

    def set(self, x, y, i):
        if not self.in_bounds(x, y):
            raise ValueError('The coordinates given (' + str(x) + ',' + str(y) + ') are out of bounds')
        self.grid[x][y].append(i)
        self.plotted_claim_ids.append(i)

    def plot_claim(self, claim: Claim):
        for row in range(claim.y, claim.y + claim.h):
            for column in range(claim.x, claim.x + claim.w):
                self.set(row, column, claim.i)

    def total_overlaps(self):
        total = 0
        for row in self.grid:
            for ids in row:
                if len(ids) > 1:
                    total += 1

        return total

    def unique_claims(self):
        already_seen = []
        counter = 0
        for row in self.grid:
            counter += 1
            for ids in row:
                if len(ids) > 1:
                    already_seen.extend(ids)
    
        return set(self.plotted_claim_ids) - set(already_seen)
