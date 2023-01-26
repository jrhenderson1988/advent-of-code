from .common import Recipes


def run(content):
    recipes = Recipes([3, 7], 0, 1)

    number = int(content)

    return recipes.get_following_10_scores_after(number)
