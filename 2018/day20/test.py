import unittest

from day20 import part01


class D20Tests(unittest.TestCase):
    a = "^WNE$"
    b = "^ENWWW(NEEE|SSE(EE|N))$"
    c = "^ENNWSWW(NEWS|)SSSEEN(WNSE|)EE(SWEN|)NNN$"
    d = "^ESSWWN(E|NNENN(EESS(WNSE|)SSS|WWWSSSSE(SW|NNNE)))$"
    e = "^WSSEESWWWNW(S|NENNEEEENN(ESSSSW(NWSW|SSEN)|WSWWN(E|WWS(E|SS))))$"

    def test_part1(self):
        self.assertEqual(3, part01.run(self.a))
        # self.assertEqual(10, part01.run(self.b))
        # self.assertEqual(18, part01.run(self.c))
        # self.assertEqual(23, part01.run(self.d))
        # self.assertEqual(31, part01.run(self.e))

    def test_part2(self):
        # TODO
        pass


if __name__ == '__main__':
    unittest.main()
