import heapq
from functools import cache


class CaveSystem:
    TORCH = 0
    CLIMBING_GEAR = 1
    NEITHER = 2

    ROCKY = 0
    WET = 1
    NARROW = 2

    def __init__(self, depth, target):
        self.depth = depth
        self.target = target

    @staticmethod
    def parse(content: str):
        lines = content.strip().split("\n")
        if not lines[0].startswith("depth:") or not lines[1].startswith("target:"):
            raise Exception("Unexpected input: %s" % content)

        depth = int(lines[0][len("depth:"):].strip())
        target = tuple(map(int, lines[1][len("target:"):].strip().split(",")))
        return CaveSystem(depth, target)

    def risk_level(self, coord):
        return self.erosion_level(coord) % 3

    @cache
    def erosion_level(self, coord):
        return (self.geologic_index(coord) + self.depth) % 20183

    @cache
    def geologic_index(self, coord):
        # The region at 0,0 (the mouth of the cave) has a geologic index of 0.
        if coord == (0, 0):
            return 0
        # The region at the coordinates of the target has a geologic index of 0.
        if coord == self.target:
            return 0
        # If the region's Y coordinate is 0, the geologic index is its X coordinate times 16807.
        if coord[1] == 0:
            return coord[0] * 16807
        # If the region's X coordinate is 0, the geologic index is its Y coordinate times 48271.
        if coord[0] == 0:
            return coord[1] * 48271
        # Otherwise, the region's geologic index is the result of multiplying the erosion levels of the regions at X-1,Y and X,Y-1.
        return self.erosion_level((coord[0] - 1, coord[1])) * self.erosion_level((coord[0], coord[1] - 1))

    def total_risk_factor_for_initial_area(self):
        return sum(self.risk_level((x, y)) for x in range(self.target[0] + 1) for y in range(self.target[1] + 1))

    def total_time_to_move_to_target(self):
        # dijkstra seems appropriate here given the different costs for all the moves
        # the neighbouring node function should return the possible next states
        # each state should include a coordinate and equipment state
        # the next state might be the same coordinate, but a change of equipment
        # target state is the target area but with the torch equipment selected
        start = ((0, 0), self.TORCH)
        target = (self.target, self.TORCH)

        distances = {start: 0}
        previous = {}
        pq = [(0, start)]
        visited = set()
        while pq:
            current_dist, current = heapq.heappop(pq)
            if current in visited:
                continue

            visited.add(current)

            if current == target:
                return distances[current]

            for neighbour, cost in self.possible_next_moves(current):
                if neighbour in visited:
                    continue

                new_dist = current_dist + cost

                # If we found a better path (or first path) to neighbor
                if neighbour not in distances or new_dist < distances[neighbour]:
                    distances[neighbour] = new_dist
                    previous[neighbour] = current
                    heapq.heappush(pq, (new_dist, neighbour))

        return 0

    def possible_next_moves(self, state):
        coord, tool = state
        next_moves = []
        for delta in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
            cx, cy = coord
            dx, dy = delta
            x, y = cx + dx, cy + dy
            if x < 0 or y < 0:
                continue

            area_type = self.risk_level((x, y))
            if area_type == self.ROCKY and (tool == self.TORCH or tool == self.CLIMBING_GEAR):
                next_moves.append((((x, y), tool), 1))
            elif area_type == self.WET and (tool == self.CLIMBING_GEAR or tool == self.NEITHER):
                next_moves.append((((x, y), tool), 1))
            elif area_type == self.NARROW and (tool == self.TORCH or tool == self.NEITHER):
                next_moves.append((((x, y), tool), 1))

        current_area_type = self.risk_level(coord)
        for next_tool in [self.CLIMBING_GEAR, self.TORCH, self.NEITHER]:
            if next_tool == tool:
                continue
            if next_tool == self.CLIMBING_GEAR and current_area_type == self.NARROW:
                continue
            if next_tool == self.TORCH and current_area_type == self.WET:
                continue
            if next_tool == self.NEITHER and current_area_type == self.ROCKY:
                continue

            next_moves.append(((coord, next_tool), 7))

        return next_moves

    def possible_tools_for_area_type(self, area_type):
        if area_type == self.ROCKY:
            return [self.TORCH, self.CLIMBING_GEAR]
        elif area_type == self.WET:
            return [self.CLIMBING_GEAR, self.NEITHER]
        elif area_type == self.NARROW:
            return [self.TORCH, self.NEITHER]
        else:
            raise Exception("Unexpected area_type: %s" % area_type)
