from .common import Program

def run(content):
    data = content.strip().splitlines()

    # takes a while (10-15 mins on my machine) but it works!

    registers = [0, 0, 0, 0, 0, 0]
    program = Program.parse(registers, data)
    possible_exit_values = set()
    last = None
    while True:
        if program.ip == 28:
            curr = program.registers[3]
            if curr in possible_exit_values:
                # if we've seen this value before, we want to return the previous value (last), so we break from the
                # loop and return
                break

            # if we haven't seen it before, set the current to the last seen value
            last = curr
            possible_exit_values.add(last)

        program.execute_current_instruction()

    return last
