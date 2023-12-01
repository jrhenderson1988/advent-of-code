class Direction:
    N = (0, -1)
    E = (1, 0)
    S = (0, 1)
    W = (-1, 0)

    def __init__(self, delta):
        self.delta = delta

    def __repr__(self):
        if self.delta == Direction.N:
            return 'N'
        elif self.delta == Direction.E:
            return 'E'
        elif self.delta == Direction.S:
            return 'S'
        elif self.delta == Direction.W:
            return 'W'
        else:
            raise ValueError('Unexpected direction')

    @staticmethod
    def from_char(ch):
        if ch == 'N':
            return Direction(Direction.N)
        elif ch == 'E':
            return Direction(Direction.E)
        elif ch == 'S':
            return Direction(Direction.S)
        elif ch == 'W':
            return Direction(Direction.W)
        return None

    def move(self, point):
        (px, py) = point
        (dx, dy) = self.delta
        return (px + dx, py + dy)


class Fork:
    def __init__(self, options):
        self.options = options

    def __repr__(self):
        s = '('
        for i in range(0, len(self.options)):
            directions = self.options[i]
            if i != 0:
                s += '|'

            for j in range(0, len(directions)):
                if isinstance(directions[j], Fork):
                    s += str(directions[j])
                else:
                    s += str(directions[j])
        s += ')'
        return s

    @staticmethod
    def parse(content):
        options = []
        directions = []
        depth = 0
        fork_start = None
        for i in range(0, len(content)):
            ch = content[i]
            if ch == '(':
                if depth == 0:
                    fork_start = i
                depth += 1
            elif ch == ')':
                depth -= 1
                if depth == 0:
                    directions.append(Fork.parse(content[fork_start + 1:i]))
            elif ch == '|':
                if depth == 0:
                    options.append(directions)
                    directions = []
            elif depth == 0:
                d = Direction.from_char(ch)
                directions.append(d)

        options.append(directions)
        return Fork(options)

    # def trace(self):
    #     _, seen = Fork._trace(self.options[0], {(0, 0)}, {(0, 0): set()})
    #     return seen
    #
    # # NEE(NE|SW)N
    #
    # @staticmethod
    # def _trace(instructions, positions, seen):
    #     for instruction in instructions:
    #         if isinstance(instruction, Direction):
    #             positions, seen = Fork._move_positions(positions, seen, instruction)
    #         if isinstance(instruction, Fork):
    #             pass
    #             # for position in positions:
    #             #     for option in instruction.options:
    #             #         new_positions, new_seen = Fork._trace(option, {position}, seen)
    #             #         positions = positions.union(new_positions)
    #             #
    #
    #     return positions, seen
    #
    # @staticmethod
    # def _move_positions(positions, seen, instruction):
    #     new_positions = set()
    #     for position in positions:
    #         new_pos = instruction.move(position)
    #         seen[position].add(new_pos)
    #         if new_pos in seen:
    #             seen[new_pos].add(position)
    #         else:
    #             seen[new_pos] = {position}
    #         new_positions.add(new_pos)
    #
    #     return new_positions, seen

    # def furthest_room_distance(self):
    #     dist, _ = self._furthest_room_distance(self.options[0], (0, 0), 0, {(0, 0): 0})
    #     return dist
    #
    # @staticmethod
    # def _furthest_room_distance(instructions, curr_pos, curr_dist, seen):
    #     for instruction in instructions:
    #         if isinstance(instruction, Direction):
    #             curr_pos = instruction.move(curr_pos)
    #             curr_dist = seen[curr_pos] if curr_pos in seen else curr_dist + 1
    #             seen[curr_pos] = curr_dist
    #         elif isinstance(instruction, Fork):
    #             pass
    #
    #     return curr_dist, seen

    def furthest_room_distance(self):
        positions, _ = Fork._furthest_room_distance(self.options[0], [((0, 0), 0)], {(0, 0): 0})
        return max(dist for [_, dist] in positions)

    @staticmethod
    def _furthest_room_distance(instructions, positions, seen):
        print("Positions: %s. Seen: %s" % (positions, seen))
        for instruction in instructions:
            if isinstance(instruction, Direction):
                positions, seen = Fork._update_positions(positions, seen, instruction)
            elif isinstance(instruction, Fork):

                # Fork off and create a position copy for each option
                # when the fork returns, we _replace_ the positions with all of those returned (all the positions
                # for each fork).
                # Every time we call fork again for each new option, we continually pass in the updated "seen"

                # we may need to take care of the case below where, the new distance calculated is LESS than what is in
                # "seen". A new path may have been calculated.

                # maybe the key here is to actually just run all the different possible paths and keep "seen" up to date
                # with the lowest possible value (when we hit something we've seen again, we take the lowest) and at the
                # end we just take the largest value in "seen"
                pass
            print("Positions: %s. Seen: %s" % (positions, seen))

        return positions, seen

    @staticmethod
    def _update_positions(positions, seen, direction):
        new_positions = []
        for position, distance in positions:
            new_position = direction.move(position)
            new_distance = distance + 1
            new_positions.append((new_position, new_distance))
            seen[new_position] = min(new_distance, seen[new_position]) if new_position in seen else distance

        return new_positions, seen



def run(content):
    content = content.strip()
    if content[0] != '^' or content[-1] != '$':
        raise ValueError("Invalid input: " + content)

    chars = [content[i] for i in range(1, len(content) - 1)]
    instructions = Fork.parse(chars)

    return instructions.furthest_room_distance()
