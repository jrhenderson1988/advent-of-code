import textwrap
import unittest

import day03.part01
import day03.part02


class D03Tests(unittest.TestCase):
    a = textwrap.dedent(
        """
        #1 @ 1,3: 4x4
        #2 @ 3,1: 4x4
        #3 @ 5,5: 2x2
        """
    )

    def test_part1(self):
        self.assertEqual(4, day03.part01.run(self.a))

    def test_part2(self):
        self.assertEqual(3, day03.part02.run(self.a))


if __name__ == '__main__':
    unittest.main()
