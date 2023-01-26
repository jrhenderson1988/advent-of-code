import unittest

import day14.part01
import day14.part02


class D14Tests(unittest.TestCase):

    def test_part1(self):
        self.assertEqual("5158916779", day14.part01.run("9"))
        self.assertEqual("0124515891", day14.part01.run("5"))
        self.assertEqual("9251071085", day14.part01.run("18"))
        self.assertEqual("5941429882", day14.part01.run("2018"))

    def test_part2(self):
        self.assertEqual(9, day14.part02.run("51589"))
        self.assertEqual(5, day14.part02.run("01245"))
        self.assertEqual(18, day14.part02.run("92510"))
        self.assertEqual(2018, day14.part02.run("59414"))


if __name__ == '__main__':
    unittest.main()
