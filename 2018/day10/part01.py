from loader import load_input
import os
from .common import Sky, Star


def run():
    stars = [Star.parse(line) for line in load_input(os.path.join(os.path.dirname(__file__), 'input.txt'))]

    sky = Sky(stars)

    return sky.draw_stars()
