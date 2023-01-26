import textwrap
import unittest

import day06.part01
import day06.part02


class D06Tests(unittest.TestCase):
    a = textwrap.dedent(
        """
        1, 1
        1, 6
        8, 3
        3, 4
        5, 5
        8, 9
        """
    )

    def test_part1(self):
        self.assertEqual(17, day06.part01.run(self.a))

    def test_part2(self):
        self.assertEqual(16, day06.part02.run(self.a, 32))


if __name__ == '__main__':
    unittest.main()
