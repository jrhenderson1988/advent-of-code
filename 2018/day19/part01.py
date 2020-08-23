from loader import load_input
from .common import Program
import os


def run():
    data = load_input(os.path.join(os.path.dirname(__file__), 'input.txt'))

    program = Program.parse([0, 0, 0, 0, 0, 0], data)
    program.execute()

    return program.registers[0]
