class Game:
    def __init__(self, num_players: int, last_marble_value: int):
        self.num_players = num_players
        self.scores = {i: 0 for i in range(num_players)}
        self.next_player = 0
        self.next_marble = 1
        self.last_marble_value = last_marble_value
        self.head = Marble(None, 0)
        self.current = self.head
        self.size = 1
        self.played = False

    @staticmethod
    def is_special_marble(marble):
        return marble.value % 23 == 0

    def is_game_over(self):
        return self.next_marble > self.last_marble_value

    def get_next_player(self):
        next_player = self.next_player
        self.next_player = (next_player + 1) % self.num_players

        return next_player

    def get_next_marble_value(self):
        next_marble = self.next_marble
        self.next_marble += 1

        return next_marble

    def get_highest_score(self):
        if not self.is_game_over():
            self.play()

        return max([score for score in self.scores.values()])

    def play(self):
        while not self.is_game_over():
            player = self.get_next_player()
            marble = self.get_next_marble_value()

            self.take_turn(player, marble)

    def take_turn(self, player: int, value: int):
        marble = Marble(player, value)
        if self.is_special_marble(marble):
            self.scoring_turn(marble)
        else:
            self.standard_turn(marble)

    def scoring_turn(self, marble):
        target = self.current.previous.previous.previous.previous.previous.previous.previous
        self.scores[marble.player] += marble.value + target.value
        before = target.previous
        after = target.next
        before.next = after
        after.previous = before
        self.current = after

    def standard_turn(self, marble):
        before = self.current.next
        after = self.current.next.next
        before.next = marble
        after.previous = marble
        marble.previous = before
        marble.next = after
        self.current = marble
        self.size += 1

    def __repr__(self):
        s = '['
        marble = self.head
        for i in range(self.size):
            s += ('(%d)' if marble is self.current else '%d') % marble.value + ', '
            marble = marble.next

        return s + ']'


class Marble:
    def __init__(self, player, value: int):
        self.player = player
        self.value = value
        self.previous = self
        self.next = self
