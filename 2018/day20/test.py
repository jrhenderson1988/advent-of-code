import unittest

import day20.part01


class D20Tests(unittest.TestCase):
    def test_part1(self):
        self.assertEqual(3, day20.part01.run("^WNE$"))
        self.assertEqual(10, day20.part01.run("^ENWWW(NEEE|SSE(EE|N))$"))
        self.assertEqual(18, day20.part01.run("^ENNWSWW(NEWS|)SSSEEN(WNSE|)EE(SWEN|)NNN$"))

    def test_part2(self):
        # TODO
        pass


if __name__ == '__main__':
    unittest.main()
