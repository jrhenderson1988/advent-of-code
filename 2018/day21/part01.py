from .common import Program

def run(content):
    # originally solved this by putting a conditional breakpoint that is hit when ip == 28 and looking at r3's value
    # (since the exit condition is effectively when r3 == r0) and executing with register_0 initially as 0. The
    # breakpoint was hit very quickly and the value left in r3 (in my case) was the right answer.

    # what follow is a more general solution (it assumes that r3 is the register in your exit condition on line 28). It
    # may not work for all inputs

    data = content.strip().splitlines()

    # execute the program once, until line 28 is reached
    registers = [0, 0, 0, 0, 0, 0]
    program = Program.parse(registers, data)
    while program.ip != 28:
        program.execute_current_instruction()

    # when we hit line 28, the exit/halt condition is when r0 == r3, so r3 must be the answer
    answer = registers[3]

    # we then check by setting our answer to be r0 and run the program again to verify that it's correct
    program = Program.parse([answer, 0, 0, 0, 0, 0], data)
    program.execute()

    return answer


def translation():
                                                 # #ip 2 (r2)
    r0, r1, r2, r3, r4, r5 = 0, 0, 0, 0, 0, 0    #
    r3 = 123                                     #  0: seti 123 0 3
    r3 = r3 & 456                                #  1: bani 3 456 3
    r3 = 1 if r3 == 72 else 0                    #  2: eqri 3 72 3
    r2 = r3 + r2                                 #  3: addr 3 2 2
    r2 = 0                                       #  4: seti 0 0 2 (jump to 0, since r2=ip)
    r3 = 0                                       #  5: seti 0 5 3
    r1 = r3 | 65536                              #  6: bori 3 65536 1
    r3 = 10373714                                #  7: seti 10373714 2 3
    r5 = r1 & 255                                #  8: bani 1 255 5
    r3 = r3 + r5                                 #  9: addr 3 5 3
    r3 = r3 & 16777215                           # 10: bani 3 16777215 3
    r3 = r3 * 65899                              # 11: muli 3 65899 3
    r3 = r3 & 16777215                           # 12: bani 3 16777215 3
    r5 = 1 if 256 > r1 else 0                    # 13: gtir 256 1 5
    r2 = r5 + r2                                 # 14: addr 5 2 2
    r2 = r2 + 1                                  # 15: addi 2 1 2 (skip next instruction)
    r2 = 27                                      # 16: seti 27 7 2 (jump to 27)
    r5 = 0                                       # 17: seti 0 3 5
    r4 = r5 + 1                                  # 18: addi 5 1 4
    r4 = r4 * 256                                # 19: muli 4 256 4
    r4 = 1 if r4 > r1 else 0                     # 20: gtrr 4 1 4
    r2 = r4 + r2                                 # 21: addr 4 2 2
    r2 = r2 + 1                                  # 22: addi 2 1 2
    r2 = 25                                      # 23: seti 25 4 2 (jump to 25)
    r5 = r5 + 1                                  # 24: addi 5 1 5
    r2 = 17                                      # 25: seti 17 0 2 (jump to 17)
    r1 = r5                                      # 26: setr 5 2 1
    r2 = 7                                       # 27: seti 7 4 2 (jump to 7)
    r5 = 1 if r3 == r0 else 0                    # 28: eqrr 3 0 5 (exit condition is r3 == r0)
    r2 = r5 + r2                                 # 29: addr 5 2 2 (exit when r5 is 1)
    r2 = 7                                       # 30: seti 5 7 2 (jump to 7)