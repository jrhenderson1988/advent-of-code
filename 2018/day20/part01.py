def run(content):
    content = content.strip()
    if content[0] != '^' or content[-1] != '$':
        raise Exception("Invalid input")

    content = content[1:-1]
    if '^' in content or '$' in content:
        raise Exception("Invalid input")

    distances = find_distances_to_rooms((0, 0), content)
    return max(distances.values())


# def trace_rooms(directions):
#     current = (0, 0)
#     points = {current}
#     for direction in directions:
#         if direction == '(':
#             raise Exception('( not yet implemented')
#         elif direction == ')':
#             raise Exception(') not yet implemented')
#         elif direction == '|':
#             raise Exception('| not yet implemented')
#
#         delta = direction_to_delta(direction)
#         current = apply_delta(current, delta)
#         points.add(current)
#
#     return points


def direction_to_delta(direction):
    if direction == 'N':
        return 0, -1
    elif direction == 'S':
        return 0, 1
    elif direction == 'E':
        return 1, 0
    elif direction == 'W':
        return -1, 0
    else:
        raise Exception("Invalid direction: %s" % direction)


def apply_delta(point, delta):
    x, y = point
    dx, dy = delta
    return x + dx, y + dy


# def furthest_room_distance(start, rooms):
#     visited = {start}
#     queue = deque([(start, 0)])
#     distances = {start: 0}
#
#     while queue:
#         node, dist = queue.popleft()
#         for neighbour in neighbouring_rooms(rooms, node):
#             if neighbour not in visited:
#                 visited.add(neighbour)
#                 distances[neighbour] = dist + 1
#                 queue.append((neighbour, dist + 1))
#
#     print("====")
#     print(distances)
#     # do a BFS to explore all the rooms and calculate their shortest distances
#     # find the highest distance and return it
#     return 0
#
#
# def neighbouring_rooms(rooms, current):
#     return filter(lambda n: n in rooms, [apply_delta(current, direction_to_delta(direction)) for direction in 'NESW'])

def find_distances_to_rooms(start, directions, distances = None):
    current = start
    distances = {current: 0} if distances is None else distances

    i = 0
    while i < len(directions):
        direction = directions[i]
        if direction == '(':
            sub_path_indices = find_sub_path_indices(i, directions)
            sub_paths = extract_sub_paths(directions, sub_path_indices)
            for sub_path in sub_paths:
                distances = find_distances_to_rooms(current, sub_path, distances)

            i = sub_path_indices[-1] + 1  # continue from the direction after the closing paren
            continue
        elif direction == ')':
            raise Exception(') not yet implemented')
        elif direction == '|':
            raise Exception('| not yet implemented')

        delta = direction_to_delta(direction)
        neighbour = apply_delta(current, delta)
        distances[neighbour] = distances[current] + 1 if neighbour not in distances else min(distances[current] + 1, distances[neighbour])
        current = neighbour
        i += 1

    # print("%s + %s -> %s" % (start, directions, distances))
    return distances


def find_sub_path_indices(index, directions):
    assert directions[index] == '('

    counter = 0
    pipe_indices = []
    for j in range(index + 1, len(directions)):
        if directions[j] == '(':
            counter += 1

        if counter == 0:
            if directions[j] == '|':
                pipe_indices.append(j)
            if directions[j] == ')':
                return [index] + pipe_indices + [j]

        if directions[j] == ')':
            counter -= 1

    raise Exception('No matching closing paren could be found from index %d' % index)


def extract_sub_paths(directions, positions):
    return [directions[(positions[i - 1] + 1): positions[i]] for i in range(1, len(positions))]
