from common import load_input
from .common import single_differing_index
import os


def run():
    lines = load_input(os.path.join(os.path.dirname(__file__), 'input.txt'))
    for i in range(0, len(lines)):
        a = lines[i].strip()
        for b in lines[i+1:]:
            b = b.strip()
            index = single_differing_index(a, b)
            if index is not False:
                return 'Index: %d, A letter: %s, B letter: %s, A: %s, B: %s' % (index, a[index], b[index], a, b)

    return 'Nothing :('
