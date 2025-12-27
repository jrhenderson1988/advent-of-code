import textwrap
import unittest

from day23 import part01 # , part02


class D23Tests(unittest.TestCase):
    INPUT = textwrap.dedent(
        """
        pos=<0,0,0>, r=4
        pos=<1,0,0>, r=1
        pos=<4,0,0>, r=3
        pos=<0,2,0>, r=1
        pos=<0,5,0>, r=3
        pos=<0,0,3>, r=1
        pos=<1,1,1>, r=1
        pos=<1,1,2>, r=1
        pos=<1,3,1>, r=1
        """
    )

    def test_part1(self):
        self.assertEqual(7, part01.run(self.INPUT))


if __name__ == '__main__':
    unittest.main()
