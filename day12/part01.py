from loader import load_input
from .common import PlantSimulator, Matcher
import os


def run():
    data = load_input(os.path.join(os.path.dirname(__file__), 'input.txt'))

    state = PlantSimulator.load(data, Matcher.load(data))

    return state.get_score(20)
