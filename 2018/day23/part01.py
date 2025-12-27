from day23.common import Nanobot


def run(content: str):
    nanobots = [Nanobot.parse(line.strip()) for line in content.strip().splitlines()]
    largest_radius = max(nanobots, key=lambda nb: nb.r)
    others = [n for n in nanobots if n is not largest_radius]
    return largest_radius.strength(others)