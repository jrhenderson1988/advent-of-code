import datetime
import re

class GuardShift:
    def __init__(self, guard, start):
        self.guard = guard
        self.start = start
        self.fell_asleep_at = None
        self.naps = []
        self.minutes_asleep = set()

    def sleeps_at(self, time: datetime.datetime):
        if self.fell_asleep_at is not None:
            raise ValueError('Guard is already asleep.')

        self.fell_asleep_at = time

    def wakes_at(self, time: datetime.datetime):
        if self.fell_asleep_at is None:
            raise ValueError('Guard is not asleep.')

        for i in range(self.fell_asleep_at.minute, time.minute):
            self.minutes_asleep.add(i)

        self.naps.append((self.fell_asleep_at, time))
        self.fell_asleep_at = None

    def get_total_minutes_asleep(self):
        total = 0
        for nap in self.naps:
            total += int((nap[1] - nap[0]).seconds / 60)

        return total

    def was_sleeping_at_minute(self, minute):
        return minute in self.minutes_asleep

    def was_sleeping_at(self, time):
        for nap in self.naps:
            if time >= nap[0] and time < nap[1]:
                return True

        return False

    def __str__(self):
        s = 'Guard #%s (starts: %s): ' % (str(self.guard), str(self.start))
        for nap in self.naps:
            s += '[%s - %s]' % (str(nap[0]), str(nap[1]))
        return s

    @staticmethod
    def build_shifts(events):
        shifts = []
        current_guard = None

        for event in sorted(events, key=lambda event: event.time):
            if event.is_begins_shift():
                if current_guard is not None:
                    if current_guard.fell_asleep_at is not None:
                        raise ValueError('Current guard is still asleep at beginning of new guard shift.')

                    shifts.append(current_guard)

                current_guard = GuardShift(event.get_guard(), event.time)

            elif event.is_falls_asleep():
                if current_guard is None:
                    raise ValueError('There is no current guard to fall asleep.')
                current_guard.sleeps_at(event.time)

            elif event.is_wakes_up():
                if current_guard is None:
                    raise ValueError('There is no current guard to wake up.')
                current_guard.wakes_at(event.time)
            
            else:
                raise ValueError('Unexpected event.')

        # Close off last event
        if current_guard is not None:
            shifts.append(current_guard)

        return shifts


class Event:
    def __init__(self, time, action):
        self.time = time
        self.action = action

    def __repr__(self):
        return ((self.time, self.action))

    def __str__(self):
        return self.time.__str__() + ' - ' + self.action

    def is_falls_asleep(self):
        return self.action.lower().find('falls asleep') > -1

    def is_wakes_up(self):
        return self.action.lower().find('wakes up') > -1

    def is_begins_shift(self):
        return self.action.lower().find('begins shift') > -1

    def get_guard(self):
        pattern = re.compile(r'^.*#(?P<id>\d+).*$')
        match = pattern.match(self.action)
        if not match:
            raise ValueError('No guard ID. Time: %s, Action: %s' % (self.time, self.action))

        return int(match.group('id'))

    @staticmethod
    def parse(string):
        pattern = re.compile(r'^\[(?P<y>\d{4})-(?P<m>\d{2})-(?P<d>\d{2})\s+(?P<h>\d+):(?P<i>\d+)\]\s+(?P<a>.+)$')
        match = pattern.match(string)
        if not match:
            raise ValueError('Could not parse event.')

        y = int(match.group('y'))
        m = int(match.group('m'))
        d = int(match.group('d'))
        h = int(match.group('h'))
        i = int(match.group('i'))
        a = match.group('a')

        time = datetime.datetime(y, m, d, h, i, 0, 0)
        return Event(time, a)