import re


class State:
    def __init__(self, initial_state: str):
        self.state, self.start = self.trim_state([v == '#' for v in initial_state], 0)
        self.generations = 0
        self.initial_state = self.state
        self.initial_start = self.start

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

    @staticmethod
    def trim_state(state: list, start: int):
        from_beginning = 0
        from_end = 0

        for i in range(0, 0 - start):
            if state[i] is False:
                from_beginning += 1
            else:
                break

        for i in range(len(state) - 1, 0, -1):
            if state[i] is False:
                from_end += 1
            else:
                break

        return state[from_beginning:-from_end] if from_end > 0 else state[from_beginning:], start + from_beginning

    def generate(self, matcher):
        state = [False, False] + self.state + [False, False]
        start = self.start - 2

        state = [matcher.matches(self.get_partial_state(i, state)) for i in range(len(state))]

        for i in range(2):
            if state[0] is False:
                start += 1
                state = state[1:]

        self.state, self.start = self.trim_state(state, start)
        self.generations += 1

    def apply_generations(self, n, matcher):
        for i in range(n):
            # TODO - The difference between previous and next scores eventually evens out to be the same (186)
            # record the previous X scores until the differences start out being the same, then use that to work out
            # the value for higher generation numbers...
            if self.state == self.initial_state and self.initial_state == self.initial_start:
                print('Initial state seen again at: %d' % i)
            self.generate(matcher)

    def get_score(self):
        score = 0
        for i in range(len(self.state)):
            if self.state[i]:
                score += self.start + i

        return score

    def __repr__(self):
        pattern = ''.join('#' if v is True else '.' for v in self.state)
        length = len(self.state)
        score = self.get_score()
        return '(%d generations, %d length, %d score) %s' % (self.generations, length, score, pattern)


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
