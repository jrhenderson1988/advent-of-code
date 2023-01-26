import unittest

import day08.part01
import day08.part02


class D08Tests(unittest.TestCase):
    a = "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2"

    def test_part1(self):
        self.assertEqual(138, day08.part01.run(self.a))

    def test_part2(self):
        self.assertEqual(66, day08.part02.run(self.a))


if __name__ == '__main__':
    unittest.main()
