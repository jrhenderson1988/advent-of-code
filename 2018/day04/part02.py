from loader import load_input
from .common import GuardShift, Event
import os


class Guard:
    def __init__(self, id):
        self.id = id
        self.shifts = []

    def add_shift(self, shift: GuardShift):
        if shift.guard != self.id:
            raise ValueError('That shift does not belong to this guard.')

        self.shifts.append(shift)

    def get_most_slept_minute_with_frequency(self):
        minutes = {}
        for shift in self.shifts:
            for minute in shift.minutes_asleep:
                minutes[minute] = 1 if minute not in minutes else minutes[minute] + 1
        
        most_lazy_minute = None
        total_shifts_slept = 0

        for minute, shifts_slept in minutes.items():
            if most_lazy_minute is None or shifts_slept > total_shifts_slept:
                most_lazy_minute = minute
                total_shifts_slept = shifts_slept

        return (most_lazy_minute, total_shifts_slept)


def run():
    input = load_input(os.path.join(os.path.dirname(__file__), 'input.txt'))
    shifts = GuardShift.build_shifts([Event.parse(event) for event in input])

    guards = {}
    for shift in shifts:
        guards[shift.guard] = Guard(shift.guard) if shift.guard not in guards else guards[shift.guard]
        guards[shift.guard].add_shift(shift)

    current_record = 0
    current_minute = None
    current_guard = None

    for guard in guards.values():
        guards_record = guard.get_most_slept_minute_with_frequency()
        if current_guard is None or guards_record[1] > current_record:
            current_guard = guard
            current_minute = guards_record[0]
            current_record = guards_record[1]

    return current_guard.id * current_minute
    