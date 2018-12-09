from .common import Game
from loader import load_input_as_string
import os


def run():
    data = load_input_as_string(os.path.join(os.path.dirname(__file__), 'input.txt'))

    game = Game.parse(data)

    return game.get_highest_score()
