import re


class State:
    def __init__(self, initial_state: str):
        self.state = [v == '#' for v in initial_state]
        self.initial_state = self.state
        self.start = 0

    @staticmethod
    def load(lines):
        start = 'initial state:'
        if not lines[0].startswith(start):
            raise ValueError('Could not load initial state')

        initial_state = lines[0][len(start):].strip()

        return State(initial_state)

    @staticmethod
    def get_partial_state(i, state):
        partial_state = []
        length = len(state)
        for j in range(i - 2, i + 3):
            if j < 0 or j >= length:
                partial_state.append(False)
            else:
                partial_state.append(state[j])

        return partial_state

    def generate(self, matcher):
        print('B: %s -> %d' % (str(self), self.start))

        state = [False, False] + self.state + [False, False]
        start = self.start - 2

        state = [matcher.matches(self.get_partial_state(i, state)) for i in range(len(state))]

        for i in range(2):
            if state[0] is False:
                start += 1
                state = state[1:]

        self.state = state
        self.start = start

        print('A: %s -> %d' % (str(self), self.start))

    def apply_generations(self, n, matcher):
        for _ in range(n):
            self.generate(matcher)

    def get_score(self):
        score = 0
        for i in range(len(self.state)):
            if self.state[i]:
                score += self.start + i

        return score

    def __repr__(self):
        return ''.join('#' if v is True else '.' for v in self.state)


class Matcher:
    def __init__(self, patterns: list):
        self.patterns = [Pattern(p) for p in patterns]

    def __repr__(self):
        return '\n'.join([str(p) for p in self.patterns])

    def matches(self, partial_state):
        for p in self.patterns:
            if p.matches(partial_state):
                return True

        return False

    @staticmethod
    def load(lines):
        patterns = []
        pattern = re.compile(r'^(?P<pattern>[\\.#]{5})\s*=>\s*(?P<result>[\\.#])$')
        for line in lines[2:]:
            match = pattern.match(line)
            if not match:
                raise ValueError('Invalid pattern %s' % line)

            if match.group('result') == '#':
                patterns.append(match.group('pattern'))

        return Matcher(patterns)


class Pattern:
    def __init__(self, pattern: str):
        if len(pattern) is not 5:
            raise ValueError('Invalid pattern %s' % pattern)

        self.pattern = [p == '#' for p in pattern]

    def matches(self, partial_state: list):
        return partial_state == self.pattern

    def __repr__(self):
        s = ''
        for v in self.pattern:
            s += '#' if v is True else '.'

        return s
