def run(content):
    coords = parse(content)
    constellations = [[coord] for coord in coords]

    while True:
        constellations, any_joined = join_constellations(constellations)
        if not any_joined:
            break

    return len(constellations)


def parse(content):
    return [[int(n.strip()) for n in line.strip().split(",")] for line in content.strip().splitlines()]


def distance(a, b):
    return abs(a[0] - b[0]) + abs(a[1] - b[1]) + abs(a[2] - b[2]) + abs(a[3] - b[3])


def join_constellations(constellations):
    new_constellations = []

    for a_idx in range(0, len(constellations)):
        a = constellations[a_idx]
        joiner_idx = None
        for b_idx in range(a_idx + 1, len(constellations)):
            b = constellations[b_idx]
            if can_join_constellations(a, b):
                joiner_idx = b_idx
                break

        if joiner_idx is not None:
            new_constellations.append(a + constellations[joiner_idx])
            for other_idx in range(a_idx + 1, len(constellations)):
                if other_idx != joiner_idx:
                    new_constellations.append(constellations[other_idx])
            return new_constellations, True
        else:
            new_constellations.append(a)

    return constellations, False


def can_join_constellations(a, b):
    for coord_a in a:
        for coord_b in b:
            if distance(coord_a, coord_b) <= 3:
                return True
    return False