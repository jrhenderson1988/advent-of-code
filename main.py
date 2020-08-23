import sys
import time
from importlib import import_module

if __name__ == '__main__':
    args = sys.argv
    if len(args) < 2:
        raise ValueError('usage: python main.py [day/part?]')

    identifier = [int(a) for a in args[1].split('/')]
    if len(identifier) != 2:
        raise ValueError('Expected 2 parts to identifier')

    module = 'day%02d.part%02d' % (identifier[0], identifier[1])
    part = import_module(module)

    start = time.time()
    print(part.run())
    print('Execution finished in %fs' % (time.time() - start))


