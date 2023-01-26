import textwrap
import unittest

import day15.part01


class D15Tests(unittest.TestCase):
    a = textwrap.dedent(
        """
        #######   
        #.G...#
        #...EG#
        #.#.#G#
        #..G#E#
        #.....#   
        ####### 
        """
    )

    b = textwrap.dedent(
        """
        #######
        #G..#E#
        #E#E.E#
        #G.##.#
        #...#E#
        #...E.#
        #######
        """
    )

    c = textwrap.dedent(
        """
        #######   
        #E..EG#
        #.#G.E#
        #E.##E#
        #G..#.#
        #..E#.#   
        #######   
        """
    )

    d = textwrap.dedent(
        """
        #######   
        #E.G#.#
        #.#G..#
        #G.#.G#   
        #G..#.#
        #...E.#
        #######   
        """
    )

    e = textwrap.dedent(
        """
        #######   
        #.E...#   
        #.#..G#
        #.###.#   
        #E#G#G#   
        #...#G#
        #######   
        """
    )

    f = textwrap.dedent(
        """
        #########
        #G......#
        #.E.#...#
        #..##..G#
        #...##..#
        #...#...#
        #.G...G.#
        #.....G.#
        #########
        """
    )

    def test_part1(self):
        self.assertEqual(27730, day15.part01.run(self.a))
        self.assertEqual(36334, day15.part01.run(self.b))
        self.assertEqual(39514, day15.part01.run(self.c))
        self.assertEqual(27755, day15.part01.run(self.d))
        self.assertEqual(28944, day15.part01.run(self.e))
        self.assertEqual(18740, day15.part01.run(self.f))

    def test_part2(self):
        pass  # TODO


if __name__ == '__main__':
    unittest.main()
