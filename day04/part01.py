from loader import load_input
from .common import GuardShift, Event
import os


def run():
    input = load_input(os.path.join(os.path.dirname(__file__), 'input.txt'))
    shifts = GuardShift.build_shifts([Event.parse(event) for event in input])

    # Get the total number of minutes slept by each guard
    guards = {}
    for shift in shifts:
        if shift.guard not in guards:
            guards[shift.guard] = 0
        guards[shift.guard] += shift.get_total_minutes_asleep()

    # Find the guard with the most minutes slept
    laziest_guard = None
    for guard, minutes_slept in guards.items():
        if laziest_guard is None or laziest_guard[1] < minutes_slept:
            laziest_guard = (guard, minutes_slept)
    
    # Find out which minute the laziest guard was asleep the most often
    minutes = {}
    for shift in shifts:
        if shift.guard == laziest_guard[0]:
            for i in range(0, 60):
                minutes[i] = 0 if i not in minutes else minutes[i] + shift.was_sleeping_at_minute(i)
    
    most_lazy_minute = None
    total_shifts_asleep_for_current_minute = 0
    for minute, shifts_asleep_at_minute in minutes.items():
        if most_lazy_minute is None or shifts_asleep_at_minute > total_shifts_asleep_for_current_minute:
            most_lazy_minute = minute
            total_shifts_asleep_for_current_minute = shifts_asleep_at_minute
    
    return most_lazy_minute * laziest_guard[0]