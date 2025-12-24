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


def find_distances_to_rooms(start, directions, distances=None):
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
        distances[neighbour] = distances[current] + 1 if neighbour not in distances else min(distances[current] + 1,
                                                                                             distances[neighbour])
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
