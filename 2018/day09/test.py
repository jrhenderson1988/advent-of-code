import unittest

import day09.part01
import day09.part02


class D09Tests(unittest.TestCase):
    a = "10 players; last marble is worth 1618 points"
    b = "13 players; last marble is worth 7999 points"
    c = "17 players; last marble is worth 1104 points"
    d = "21 players; last marble is worth 6111 points"
    e = "30 players; last marble is worth 5807 points"

    def test_part1(self):
        self.assertEqual(8317, day09.part01.run(self.a))
        self.assertEqual(146373, day09.part01.run(self.b))
        self.assertEqual(2764, day09.part01.run(self.c))
        self.assertEqual(54718, day09.part01.run(self.d))
        self.assertEqual(37305, day09.part01.run(self.e))

    def test_part2(self):
        self.assertEqual(74765078, day09.part02.run(self.a))


if __name__ == '__main__':
    unittest.main()
