import unittest

import day11.part01
import day11.part02


class D11Tests(unittest.TestCase):
    def test_part1(self):
        self.assertEqual("33,45", day11.part01.run("18"))
        self.assertEqual("21,61", day11.part01.run("42"))

    def test_part2(self):
        self.assertEqual("90,269,16", day11.part02.run("18"))
        self.assertEqual("232,251,12", day11.part02.run("42"))


if __name__ == '__main__':
    unittest.main()
