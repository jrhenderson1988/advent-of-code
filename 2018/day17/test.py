import textwrap
import unittest

import day17.part01
import day17.part02


class D17Tests(unittest.TestCase):
    a = textwrap.dedent(
        """
        x=495, y=2..7
        y=7, x=495..501
        x=501, y=3..7
        x=498, y=2..4
        x=506, y=1..2
        x=498, y=10..13
        x=504, y=10..13
        y=13, x=498..504
        """
    )

    def test_part1(self):
        self.assertEqual(57, day17.part01.run(self.a))

    def test_part2(self):
        self.assertEqual(29, day17.part02.run(self.a))


if __name__ == '__main__':
    unittest.main()
