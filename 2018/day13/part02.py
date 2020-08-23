from loader import load_input_as_string
from .common import Track
import os


def run():
    track = Track.parse(load_input_as_string(os.path.join(os.path.dirname(__file__), 'input.txt'), trim=False))

    return track.find_last_cart()
