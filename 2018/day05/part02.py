from string import ascii_lowercase

from .common import reduce, remove_units


def run(content):
    shortest_length = len(content)
    for c in ascii_lowercase:
        length = len(reduce(remove_units(content, c)))
        if length < shortest_length:
            shortest_length = length

    return shortest_length
