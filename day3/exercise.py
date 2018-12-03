import io
import re
from Claim import Claim
from Grid import Grid

claims = []
with open('input.txt', 'r') as f:
    for line in f:
        claims.append(Claim.parse(line))

grid = Grid(1000, 1000)
for claim in claims:
    grid.plot_claim(claim)

print(grid.total_overlaps())
print(grid.unique_claims())

# grid = Grid(10, 10)
# grid.plot_claim(Claim.parse('#1 @ 0,0: 2x2'))
# # grid.plot_claim(Claim.parse('#2 @ 1,1: 3x2'))
# grid.plot_claim(Claim.parse('#5 @ 0,0: 1x2'))
# print(grid)
# print(grid.get(0, 0))
# print(grid.get(0, 1))
# print(grid.get(1, 0))
# print(grid.get(1, 1))
# # print(grid.get(1, 3))
# # print(grid.get(1, 4))