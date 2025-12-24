from .common import find_distances_to_rooms


def run(content):
    content = content.strip()
    if content[0] != '^' or content[-1] != '$':
        raise Exception("Invalid input")

    content = content[1:-1]
    if '^' in content or '$' in content:
        raise Exception("Invalid input")

    distances = find_distances_to_rooms((0, 0), content)
    return len(list(filter(lambda d: d >= 1000, distances.values())))
