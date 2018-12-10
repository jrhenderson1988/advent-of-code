from loader import load_input_as_string
from re import compile
import os
from .common import Game


def run():
    data = load_input_as_string(os.path.join(os.path.dirname(__file__), 'input.txt'))
    pattern = compile(r'^(?P<players>\d+) players; last marble is worth (?P<points>\d+) points$')
    match = pattern.match(data)
    players = int(match.group('players'))
    points = int(match.group('points'))

    game = Game(players, points * 100)

    return game.get_highest_score()

