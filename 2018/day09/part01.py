from re import compile

from .common import Game


def run(content):
    pattern = compile(r'^(?P<players>\d+) players; last marble is worth (?P<points>\d+) points$')
    match = pattern.match(content)
    players = int(match.group('players'))
    points = int(match.group('points'))

    game = Game(players, points)

    return game.get_highest_score()
