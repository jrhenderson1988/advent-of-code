from day02.common import exactly_x, get_occurrences


def run(content):
    two = 0
    three = 0

    for line in content.strip().splitlines():
        occurrences = get_occurrences(line)
        two += (1 if exactly_x(occurrences, 2) else 0)
        three += (1 if exactly_x(occurrences, 3) else 0)

    return two * three
