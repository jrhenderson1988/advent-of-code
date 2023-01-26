from .common import Track


def run(content):
    track = Track.parse(content)

    x, y = track.find_first_collision()
    return '%d,%d' % (x, y)
