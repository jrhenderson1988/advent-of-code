from loader import load_input_as_string
import os
from .common import Node


def run():
    data = [int(d) for d in load_input_as_string(os.path.join(os.path.dirname(__file__), 'input.txt')).split(' ')]

    tree = Node.load(data)

    return tree.calculate_checksum()
