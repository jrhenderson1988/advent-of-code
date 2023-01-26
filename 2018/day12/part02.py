from .common import PlantSimulator, Matcher


def run(content):
    data = content.strip().splitlines()

    state = PlantSimulator.load(data, Matcher.load(data))

    return state.get_score(50000000000)
