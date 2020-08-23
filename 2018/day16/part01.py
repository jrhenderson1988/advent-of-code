from loader import load_input
from .common import SampleProcessor
import os


def run():
    data = load_input(os.path.join(os.path.dirname(__file__), 'input.txt'))

    sp = SampleProcessor.parse(data)

    return sp.get_num_samples_with_n_or_more_possible_methods(3)
