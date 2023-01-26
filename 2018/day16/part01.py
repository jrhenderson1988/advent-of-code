from .common import SampleProcessor


def run(content):
    data = content.strip().splitlines()

    sp = SampleProcessor.parse(data)

    return sp.get_num_samples_with_n_or_more_possible_methods(3)
