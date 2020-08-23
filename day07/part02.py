from loader import load_input
from .common import DependencyTree
import os


def run():
    tree = DependencyTree.parse(load_input(os.path.join(os.path.dirname(__file__), 'input.txt')))
    path, time = tree.resolve_for_workers(5, 60)
    return time
