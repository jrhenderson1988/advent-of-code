from .common import Game
from loader import load_input_as_string
import os
from re import compile


def run():
    data = load_input_as_string(os.path.join(os.path.dirname(__file__), 'input.txt'))
    pattern = compile(r'^(?P<players>\d+) players; last marble is worth (?P<points>\d+) points$')
    match = pattern.match(data)
    players = int(match.group('players'))
    points = int(match.group('points'))

    game = Game(players, points)

    return game.get_highest_score()
