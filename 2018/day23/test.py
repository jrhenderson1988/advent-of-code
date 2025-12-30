import textwrap
import unittest

from day23 import part01, part02


class D23Tests(unittest.TestCase):
    def test_part1(self):
        input = textwrap.dedent(
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
        self.assertEqual(7, part01.run(input))

    def test_part2(self):
        input = textwrap.dedent(
            """
            pos=<10,12,12>, r=2
            pos=<12,14,12>, r=2
            pos=<16,12,12>, r=4
            pos=<14,14,14>, r=6
            pos=<50,50,50>, r=200
            pos=<10,10,10>, r=5
            """
        )
        self.assertEqual(36, part02.run(input))


if __name__ == '__main__':
    unittest.main()
