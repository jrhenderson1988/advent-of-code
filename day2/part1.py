from day2.common import exactly_x, get_occurrences
from loader import load_input
import os


def run():
    two = 0
    three = 0

    for line in load_input(os.path.join(os.path.dirname(__file__), 'input.txt')):
        occurrences = get_occurrences(line)
        two += (1 if exactly_x(occurrences, 2) else 0)
        three += (1 if exactly_x(occurrences, 3) else 0)

    return two * three
