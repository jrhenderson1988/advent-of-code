from .common import Program


def run(content):
    data = content.strip().splitlines()

    program = Program.parse([0, 0, 0, 0, 0, 0], data)
    program.execute()

    return program.registers[0]
