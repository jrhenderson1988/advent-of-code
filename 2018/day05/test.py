import unittest

import day05.part01
import day05.part02


class D05Tests(unittest.TestCase):
    def test_part1(self):
        self.assertEqual(10, day05.part01.run("dabAcCaCBAcCcaDA"))

    def test_part2(self):
        self.assertEqual(4, day05.part02.run("dabAcCaCBAcCcaDA"))


if __name__ == '__main__':
    unittest.main()
