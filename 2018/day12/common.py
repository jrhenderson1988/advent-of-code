import re


class PlantSimulator:
    def __init__(self, state: str, matcher, repeated_difference_threshold=10):
        self.state, self.start = self.trim_state([v == '#' for v in state], 0)
        self.matcher = matcher
        self.repeated_difference_threshold = repeated_difference_threshold

    @staticmethod
    def load(lines, matcher):
        start = 'initial state:'
        if not lines[0].startswith(start):
            raise ValueError('Could not load initial state')

        state = lines[0][len(start):].strip()

        return PlantSimulator(state, matcher)

    @staticmethod
    def get_partial_state(i: int, state: list):
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

    @staticmethod
    def calculate_score(state: list, start: int):
        score = 0
        for i in range(len(state)):
            if state[i]:
                score += start + i

        return score

    @staticmethod
    def calculate_advanced_score(curr_score: int, curr_generation: int, difference: int, target_generation: int):
        return curr_score + ((target_generation - curr_generation) * difference)

    def generate(self, state: list, start: int):
        state = [False, False] + state + [False, False]
        start = start - 2

        state = [self.matcher.matches(PlantSimulator.get_partial_state(i, state)) for i in range(len(state))]

        for i in range(2):
            if state[0] is False:
                start += 1
                state = state[1:]

        return PlantSimulator.trim_state(state, start)

    def get_score(self, num_generations):
        state = self.state
        start = self.start

        prev_score = self.calculate_score(state, start)
        prev_diff = prev_score
        occurrences = 1

        for i in range(num_generations):
            state, start = self.generate(state, start)
            curr_score = self.calculate_score(state, start)
            curr_diff = curr_score - prev_score
            if prev_diff == curr_diff:
                occurrences += 1
            else:
                prev_diff = curr_diff
                occurrences = 1

            if occurrences > self.repeated_difference_threshold:
                return self.calculate_advanced_score(curr_score, i + 1, curr_diff, num_generations)

            prev_score = curr_score

        return prev_score


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
