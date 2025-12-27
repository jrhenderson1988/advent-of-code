from day22.common import CaveSystem


def run(content):
    cs = CaveSystem.parse(content)
    return cs.total_time_to_move_to_target()
