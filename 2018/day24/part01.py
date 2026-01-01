from day24.common import Battle


def run(content):
    battle = Battle.parse(content)
    return battle.fight()
