from day24.common import Battle


def run(content):
    battle = Battle.parse(content)
    return battle.units_remaining_after_minimum_required_immune_system_boost()
