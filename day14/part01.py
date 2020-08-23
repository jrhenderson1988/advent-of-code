from loader import load_input_as_string
from .common import Recipes
import os


def run():
    recipes = Recipes([3, 7], 0, 1)

    number = int(load_input_as_string(os.path.join(os.path.dirname(__file__), 'input.txt')))

    return recipes.get_following_10_scores_after(number)
