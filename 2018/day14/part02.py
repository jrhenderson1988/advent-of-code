from .common import Recipes


def run(content):
    recipes = Recipes([3, 7], 0, 1)

    number = content

    return recipes.get_total_recipes_before(number)
