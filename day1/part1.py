from loader import load_input
import os


def run():
    frequency = 0
    for line in load_input(os.path.join(os.path.dirname(__file__), 'input.txt')):
        frequency += int(line)

    return frequency
