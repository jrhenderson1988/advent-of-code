from loader import load_input_as_string
from .common import reduce
import os


def run():
    value = load_input_as_string(os.path.join(os.path.dirname(__file__), 'input.txt'))
    return len(reduce(value))