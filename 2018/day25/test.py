import textwrap
import unittest

from day25 import part01


class D25Tests(unittest.TestCase):
    def test_part1(self):
        self.assertEqual(2, part01.run(textwrap.dedent(
            """
             0,0,0,0
             3,0,0,0
             0,3,0,0
             0,0,3,0
             0,0,0,3
             0,0,0,6
             9,0,0,0
            12,0,0,0
            """
        )))
        self.assertEqual(4, part01.run(textwrap.dedent(
            """
            -1,2,2,0
            0,0,2,-2
            0,0,0,-2
            -1,2,0,0
            -2,-2,-2,2
            3,0,2,-1
            -1,3,2,2
            -1,0,-1,0
            0,2,1,-2
            3,0,0,0
            """
        )))
        self.assertEqual(3, part01.run(textwrap.dedent(
            """
            1,-1,0,1
            2,0,-1,0
            3,2,-1,0
            0,0,3,1
            0,0,-1,-1
            2,3,-2,0
            -2,2,0,0
            2,-2,0,-1
            1,-1,0,-1
            3,2,0,2
            """
        )))
        self.assertEqual(8, part01.run(textwrap.dedent(
            """
            1,-1,-1,-2
            -2,-2,0,1
            0,2,1,3
            -2,3,-2,1
            0,2,3,-2
            -1,-1,1,-2
            0,-2,-1,0
            -2,2,3,-1
            1,2,2,0
            -1,-2,0,-2
            """
        )))

    def test_part2(self):
        pass # no part 2


if __name__ == '__main__':
    unittest.main()
