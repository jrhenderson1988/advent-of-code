import sys
from importlib import import_module

if __name__ == '__main__':
    args = sys.argv
    if len(args) < 3:
        raise ValueError('usage: python main.py [day] [part]')

    module = args[1] + '.' + args[2]
    part = import_module(module)
    print(part.run())


