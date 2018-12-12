from loader import load_input
from .common import State, Matcher
import os


def run():
    data = load_input(os.path.join(os.path.dirname(__file__), 'input.txt'))
    state = State.load(data)
    matcher = Matcher.load(data)
    state.apply_generations(20, matcher)

    return state.get_score()
