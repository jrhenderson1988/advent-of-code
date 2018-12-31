import re


class State:
    BEFORE = 1
    AFTER = 2

    def __init__(self, when: int, r0: int, r1: int, r2: int, r3: int):
        self.when = when
        self.r0 = r0
        self.r1 = r1
        self.r2 = r2
        self.r3 = r3

    def __repr__(self):
        when = 'Before' if self.when == State.BEFORE else 'After'
        return '%s: [%d, %d, %d, %d]' % (when, self.r0, self.r1, self.r2, self.r3)

    @staticmethod
    def parse(when: int, line):
        pattern = re.compile(r'^(Before|After):\s*\[\s*(\d+),\s*(\d+),\s*(\d+),\s*(\d+)\s*\]$')
        match = pattern.match(line)
        if match is not None:
            w, r0, r1, r2, r3 = match.groups()
            if when == State.BEFORE and w.lower() == 'before':
                return State(State.BEFORE, int(r0), int(r1), int(r2), int(r3))
            elif when == State.AFTER and w.lower() == 'after':
                return State(State.AFTER, int(r0), int(r1), int(r2), int(r3))

        return None

    @staticmethod
    def parse_after(line):
        return State.parse(State.AFTER, line)

    @staticmethod
    def parse_before(line):
        return State.parse(State.BEFORE, line)


class Instruction:
    def __init__(self, o: int, a: int, b: int, c: int):
        self.o = o
        self.a = a
        self.b = b
        self.c = c

    @staticmethod
    def parse(line):
        pattern = re.compile(r'^\s*(\d+)\s+(\d+)\s+(\d+)\s+(\d+)\s*$')
        match = pattern.match(line)
        if match:
            o, a, b, c = match.groups()
            return Instruction(int(o), int(a), int(b), int(c))

        return None


class SampleProcessor:
    def __init__(self):
        self.op = OpCodeProcessor()

    def get_possible_methods(self, before: State, instruction: Instruction, after: State):
        possible_methods = []
        for m in OpCodeProcessor.METHODS:
            self.op.set_all(before.r0, before.r1, before.r2, before.r3)
            method = getattr(self.op, m)
            if method is None:
                raise ValueError('Method %s does not exist.' % m)

            method(instruction.a, instruction.b, instruction.c)
            if self.op.matches(after.r0, after.r1, after.r2, after.r3):
                possible_methods.append(m)

        return possible_methods

    def get_total_possible_methods(self, before: State, instruction: Instruction, after: State):
        return len(self.get_possible_methods(before, instruction, after))



class OpCodeProcessor:

    METHODS = [
        'addr', 'addi',
        'mulr', 'muli',
        'banr', 'bani',
        'borr', 'bori',
        'setr', 'seti',
        'gtir', 'gtri', 'gtrr',
        'eqir', 'eqri', 'eqrr'
    ]

    def __init__(self):
        self.registers = [0, 0, 0, 0]

    def __repr__(self):
        return '(' + ', '.join([str(v) for v in self.registers]) + ')'

    def validate_register(self, register: int):
        if register < 0 or register > len(self.registers):
            raise ValueError('Invalid register: %d.' % register)

    def set(self, register: int, value: int):
        self.validate_register(register)
        self.registers[register] = value

    def get(self, register: int):
        self.validate_register(register)
        return self.registers[register]

    def set_all(self, r0: int, r1: int, r2: int, r3: int):
        self.registers = [r0, r1, r2, r3]

    def get_all(self):
        return self.registers

    def matches(self, r0: int, r1: int, r2: int, r3: int):
        return self.registers == [r0, r1, r2, r3]

    def addr(self, a: int, b: int, c: int):
        self.set(c, self.get(a) + self.get(b))

    def addi(self, a: int, b: int, c: int):
        self.set(c, self.get(a) + b)

    def mulr(self, a: int, b: int, c: int):
        self.set(c, self.get(a) * self.get(b))

    def muli(self, a: int, b: int, c: int):
        self.set(c, self.get(a) * b)

    def banr(self, a: int, b: int, c: int):
        self.set(c, self.get(a) & self.get(b))

    def bani(self, a: int, b: int, c: int):
        self.set(c, self.get(a) & b)

    def borr(self, a: int, b: int, c: int):
        self.set(c, self.get(a) | self.get(b))

    def bori(self, a: int, b: int, c: int):
        self.set(c, self.get(a) | b)

    def setr(self, a: int, b: int, c: int):
        self.set(c, self.get(a))

    def seti(self, a: int, b: int, c: int):
        self.set(c, a)

    def gtir(self, a: int, b: int, c: int):
        self.set(c, 1 if a > self.get(b) else 0)

    def gtri(self, a: int, b: int, c: int):
        self.set(c, 1 if self.get(a) > b else 0)

    def gtrr(self, a: int, b: int, c: int):
        self.set(c, 1 if self.get(a) > self.get(b) else 0)

    def eqir(self, a: int, b: int, c: int):
        self.set(c, 1 if a == self.get(b) else 0)

    def eqri(self, a: int, b: int, c: int):
        self.set(c, 1 if self.get(a) == b else 0)

    def eqrr(self, a: int, b: int, c: int):
        self.set(c, 1 if self.get(a) == self.get(b) else 0)