from .common import Sky, Star


def run(content):
    stars = [Star.parse(line) for line in content.strip().splitlines()]

    sky = Sky(stars)

    return sky.draw_stars()
