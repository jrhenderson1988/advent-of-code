from common import load_input
import os


def run():
    seen = {0}
    frequency = 0
    while True:
        for line in load_input(os.path.join(os.path.dirname(__file__), 'input.txt')):
            frequency += int(line)
            if frequency in seen:
                return frequency
            else:
                seen.add(frequency)
