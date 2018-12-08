from loader import load_input_as_string
import os
from .common import Node

def run():
    # data = [int(d) for d in load_input_as_string(os.path.join(os.path.dirname(__file__), 'input.txt')).split(' ')]
    data = [2, 3, 0, 3, 10, 11, 12, 1, 1, 0, 1, 99, 2, 1, 1, 2]

    tree = Node.load(data)
    print(tree)
    return tree.calculate_value()