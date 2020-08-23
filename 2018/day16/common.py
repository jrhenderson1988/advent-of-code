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

    def __repr__(self):
        return '%d %d %d %d' % (self.o, self.a, self.b, self.c)

    @staticmethod
    def parse(line):
        pattern = re.compile(r'^\s*(\d+)\s+(\d+)\s+(\d+)\s+(\d+)\s*$')
        match = pattern.match(line)
        if match:
            o, a, b, c = match.groups()
            return Instruction(int(o), int(a), int(b), int(c))

        return None


class Sample:
    def __init__(self, identifier: int, before: State, instruction: Instruction, after: State):
        self.identifier = identifier
        self.before = before
        self.instruction = instruction
        self.after = after

    def __repr__(self):
        return '%s\n%s\n%s' % (self.before, self.instruction, self.after)


class Program:
    def __init__(self, instructions: list):
        self.instructions = instructions
        self.op_code_processor = OpCodeProcessor()

    def run(self, op_code_mappings: dict):
        for instruction in self.instructions:
            method = getattr(self.op_code_processor, op_code_mappings[instruction.o])
            method(instruction.a, instruction.b, instruction.c)

    def get_final_state(self):
        return self.op_code_processor.get_all()

    @staticmethod
    def parse(data):
        instructions = []

        in_sample = False
        for line in data:
            if State.parse_before(line) is not None:
                in_sample = True
            elif State.parse_after(line) is not None:
                in_sample = False

            instruction = Instruction.parse(line)
            if not in_sample and instruction is not None:
                instructions.append(instruction)

        return Program(instructions)


class SampleProcessor:
    def __init__(self, samples: list):
        self.samples = samples
        self.op = OpCodeProcessor()

    def get_possible_methods(self, sample: Sample):
        possible_methods = []
        for m in OpCodeProcessor.METHODS:
            self.op.set_all(sample.before.r0, sample.before.r1, sample.before.r2, sample.before.r3)
            method = getattr(self.op, m)
            if method is None:
                raise ValueError('Method %s does not exist.' % m)

            method(sample.instruction.a, sample.instruction.b, sample.instruction.c)
            if self.op.matches(sample.after.r0, sample.after.r1, sample.after.r2, sample.after.r3):
                possible_methods.append(m)

        return possible_methods

    def get_total_possible_methods(self, sample: Sample):
        return len(self.get_possible_methods(sample))

    def get_num_samples_with_n_or_more_possible_methods(self, n: int):
        num_samples = 0
        for sample in self.samples:
            if self.get_total_possible_methods(sample) >= n:
                num_samples += 1

        return num_samples

    @staticmethod
    def reduce_possibilities(op_code_map, possibilities):
        queue = [(oc, list(methods)[0]) for oc, methods in possibilities.items() if len(methods) == 1]
        while len(queue) > 0:
            op_code, method = queue.pop(0)
            op_code_map[op_code] = method

            if op_code in possibilities:
                del possibilities[op_code]

            for oc in possibilities:
                if method in possibilities[oc]:
                    possibilities[oc].remove(method)
                    if len(possibilities[oc]) == 1:
                        queue.append((oc, list(possibilities[oc])[0]))

        return op_code_map, possibilities

    def discover_op_codes(self):
        op_code_map = {}
        possibilities = {}
        for sample in self.samples:
            op_code = sample.instruction.o
            if op_code in op_code_map:
                continue

            methods = self.get_possible_methods(sample)
            if len(methods) == 1:
                possibilities[op_code] = {methods[0]}
                op_code_map, possibilities = self.reduce_possibilities(op_code_map, possibilities)

            elif len(methods) > 1:
                existing = possibilities[op_code] if op_code in possibilities else set()
                new_values = set([m for m in methods if m not in op_code_map.values()])
                possibilities[op_code] = existing.union(new_values)

        op_code_map, possibilities = self.reduce_possibilities(op_code_map, possibilities)

        return op_code_map

    @staticmethod
    def parse(lines):
        samples = []
        next_id = 0
        for i in range(len(lines)):
            before = State.parse_before(lines[i])
            if before is not None and before.when == State.BEFORE:
                instruction = Instruction.parse(lines[i + 1])
                after = State.parse_after(lines[i + 2])
                if after is None or instruction is None:
                    raise ValueError('Invalid sample detected.')

                samples.append(Sample(next_id, before, instruction, after))
                next_id += 1

        return SampleProcessor(samples)


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