from .common import Track


def run(content):
    track = Track.parse(content)

    x, y = track.find_last_cart()
    return '%d,%d' % (x, y)
