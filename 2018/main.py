import sys
import time
from importlib import import_module


def read_input(day):
    with open('inputs/d%02d.txt' % day) as f:
        return f.read()


if __name__ == '__main__':
    args = sys.argv
    if len(args) < 2:
        raise ValueError('usage: python main.py [day/part?]')

    identifier = args[1].split('/')
    if len(identifier) < 1:
        raise ValueError('Expected 2 parts to identifier')

    day = int(identifier[0])
    parts = [int(identifier[1])] if len(identifier) > 1 else [1, 2]

    content = read_input(day)
    for part in parts:
        module_name = 'day%02d.part%02d' % (day, part)
        module = import_module(module_name)

        start = time.time()
        print('=== Day %d / Part %d ===' % (day, part))
        print('-> %s' % module.run(content))
        print('Execution finished in %fs' % (time.time() - start))


