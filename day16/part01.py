from loader import load_input
from .common import SampleProcessor, State, Instruction
import os


def run():
    data = load_input(os.path.join(os.path.dirname(__file__), 'input.txt'))

    sp = SampleProcessor()
    total = 0
    for i in range(len(data)):
        before = State.parse_before(data[i])
        if before is not None and before.when == State.BEFORE:
            instruction = Instruction.parse(data[i+1])
            after = State.parse_after(data[i+2])
            if after is None or instruction is None:
                raise ValueError('Uh oh %s, %s' % (str(instruction), after))

            if sp.get_total_possible_methods(before, instruction, after) >= 3:
                total += 1

    return total
