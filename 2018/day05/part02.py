from .common import reduce, remove_units
from loader import load_input_as_string
from string import ascii_lowercase
import os


def run():
    s = load_input_as_string(os.path.join(os.path.dirname(__file__), 'input.txt'))
    shortest_length = len(s)
    for c in ascii_lowercase:
        length = len(reduce(remove_units(s, c)))
        if length < shortest_length:
            shortest_length = length

    return shortest_length
