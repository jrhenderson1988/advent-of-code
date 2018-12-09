from re import compile


class Game:
    def __init__(self, num_players: int, last_marble_value: int):
        self.scores = {i: 0 for i in range(num_players)}
        self.next_player = 0
        self.last_marble_value = last_marble_value
        self.next_marble = 1
        self.marbles = [0]
        self.current_position = 0

    def play(self):
        while not self.is_game_over():
            marble = self.get_next_marble()
            player = self.get_next_player()
            position = self.get_next_position()

            if self.is_special_marble(marble):
                self.score(player, marble)
            else:
                self.place_marble_at_position(marble, position)

    def get_next_position(self):
        total_marbles = len(self.marbles)
        if total_marbles < 2:
            return 1

        index = self.current_position + 2
        if index > total_marbles:
            index = index % total_marbles

        return index

    def get_next_player(self):
        next_player = self.next_player
        num_players = len(self.scores)
        self.next_player = (next_player + 1) % num_players

        return next_player

    def is_game_over(self):
        return self.next_marble > self.last_marble_value

    @staticmethod
    def is_special_marble(marble):
        return marble % 23 == 0

    def place_marble_at_position(self, marble, position):
        self.marbles = self.marbles[:position] + [marble] + self.marbles[position:]
        self.current_position = position

    def score(self, player, marble):
        target_position = (self.current_position - 7) % len(self.marbles)
        target_marble = self.marbles[target_position]
        self.scores[player] += marble + target_marble
        self.marbles = self.marbles[:target_position] + self.marbles[target_position + 1:]
        self.current_position = target_position

    def get_next_marble(self):
        next_marble = self.next_marble
        self.next_marble += 1

        return next_marble

    def get_highest_score(self):
        if not self.is_game_over():
            self.play()

        return max([score for score in self.scores.values()])

    def __repr__(self):
        return ', '.join([('(%d)' % m) if i == self.current_position else str(m) for i, m in enumerate(self.marbles)])

    def __str__(self):
        return self.__repr__()

    @staticmethod
    def parse(data, last_marble_multiplier=1):
        pattern = compile(r'(?P<players>\d+) players; last marble is worth (?P<points>\d+) points')
        match = pattern.match(data)
        if not match:
            raise ValueError('Invalid data provided')

        return Game(int(match.group('players')), int(match.group('points')) * last_marble_multiplier)
