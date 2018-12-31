from loader import load_input
from .common import SampleProcessor, Program
import os


def run():
    data = load_input(os.path.join(os.path.dirname(__file__), 'input.txt'))

    sample_processor = SampleProcessor.parse(data)
    program = Program.parse(data)

    program.run(sample_processor.discover_op_codes())

    return program.get_final_state()[0]
