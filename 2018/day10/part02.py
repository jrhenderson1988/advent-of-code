from .common import Sky, Star


def run(content):
    stars = [Star.parse(line) for line in content.strip().splitlines()]
    sky = Sky(stars)
    sky.align_stars()

    return sky.iterations
