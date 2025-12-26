import re


class Instruction:
    def __init__(self, name: str, a: int, b: int, c: int):
        self.name = name
        self.a = a
        self.b = b
        self.c = c

    def __repr__(self):
        return '%s %d %d %d' % (self.name, self.a, self.b, self.c)

    @staticmethod
    def parse(line):
        match = re.compile(r'^\s*([a-zA-Z]+)\s+(\d+)\s+(\d+)\s+(\d+)\s*$').match(line)
        if not match:
            raise ValueError('Invalid instruction: %s' % line)
        groups = match.groups()
        return Instruction(str(groups[0]), int(groups[1]), int(groups[2]), int(groups[3]))


class Program:
    def __init__(self, registers: list, ip: int, instructions: list):
        self.registers = registers
        self.ip = 0
        self.ip_register = ip
        self.instructions = instructions

    def __repr__(self):
        return '#ip=%d %s' % (self.ip_register, str(self.registers)) + \
            '\n' + \
            '\n'.join([str(i) for i in self.instructions])

    @staticmethod
    def parse(registers: list, lines: list):
        ip_match = re.compile(r'^\s*#ip\s*(\d+)\s*$').match(lines[0])
        if not ip_match:
            raise ValueError('Invalid instruction pointer: %s' % lines[0])

        instructions = [Instruction.parse(line) for line in lines[1:]]

        return Program(registers, int(ip_match.groups()[0]), instructions)

    def execute_instruction(self, instruction: Instruction):
        method = getattr(self, 'instruction_' + instruction.name)
        if not method:
            raise NotImplementedError('Method %s does not exist.' % method)

        return method(instruction)

    def next_instruction(self):
        return self.instructions[self.ip] if 0 <= self.ip < len(self.instructions) else None

    def state_repr(self):
        return 'ip=%d %s' % (self.ip, str(self.registers))

    def execute(self):
        i = 0
        while True:
            # Get the instruction that the instruction pointer points to
            instruction = self.instructions[self.ip] if 0 <= self.ip < len(self.instructions) else None
            if instruction is None:
                break

            # Write instruction pointer value to the bound register before instruction
            self.registers[self.ip_register] = self.ip

            # pre_instruction = self.state_repr()

            # Execute the instruction if there is one, otherwise break out of the loop and end execution
            self.execute_instruction(instruction)

            # Write the value of the bound register back to the instruction pointer
            self.ip = self.registers[self.ip_register]

            # Increment the instruction pointer
            self.ip += 1

            # post_instruction = self.state_repr()

            i += 1

            # print('%s %s %s' % (pre_instruction, str(instruction), post_instruction))

    def instruction_addr(self, i: Instruction):
        self.registers[i.c] = self.registers[i.a] + self.registers[i.b]

    def instruction_addi(self, i: Instruction):
        self.registers[i.c] = self.registers[i.a] + i.b

    def instruction_mulr(self, i: Instruction):
        self.registers[i.c] = self.registers[i.a] * self.registers[i.b]

    def instruction_muli(self, i: Instruction):
        self.registers[i.c] = self.registers[i.a] * i.b

    def instruction_banr(self, i: Instruction):
        self.registers[i.c] = self.registers[i.a] & self.registers[i.b]

    def instruction_bani(self, i: Instruction):
        self.registers[i.c] = self.registers[i.a] & i.b

    def instruction_borr(self, i: Instruction):
        self.registers[i.c] = self.registers[i.a] | self.registers[i.b]

    def instruction_bori(self, i: Instruction):
        self.registers[i.c] = self.registers[i.a] | i.b

    def instruction_setr(self, i: Instruction):
        self.registers[i.c] = self.registers[i.a]

    def instruction_seti(self, i: Instruction):
        self.registers[i.c] = i.a

    def instruction_gtir(self, i: Instruction):
        self.registers[i.c] = 1 if i.a > self.registers[i.b] else 0

    def instruction_gtri(self, i: Instruction):
        self.registers[i.c] = 1 if self.registers[i.a] > i.b else 0

    def instruction_gtrr(self, i: Instruction):
        self.registers[i.c] = 1 if self.registers[i.a] > self.registers[i.b] else 0

    def instruction_eqir(self, i: Instruction):
        self.registers[i.c] = 1 if i.a == self.registers[i.b] else 0

    def instruction_eqri(self, i: Instruction):
        self.registers[i.c] = 1 if self.registers[i.a] == i.b else 0

    def instruction_eqrr(self, i: Instruction):
        self.registers[i.c] = 1 if self.registers[i.a] == self.registers[i.b] else 0
