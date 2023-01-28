import textwrap
import unittest

import day15.part01
import day15.part02


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

    actual = textwrap.dedent(
        """
        ################################
        #################.....##########
        #################..#.###########
        #################.........######
        ##################......########
        #################G.GG###########
        ###############...#..###########
        ###############......G..########
        ############..G.........########
        ##########.G.....G......########
        ##########......#.........#..###
        ##########...................###
        #########G..G.#####....E.G.E..##
        ######..G....#######...........#
        #######.....#########.........##
        #######..#..#########.....#.####
        ##########..#########..G.##..###
        ###########G#########...E...E.##
        #########.G.#########..........#
        #########GG..#######.......##.E#
        ######.G......#####...##########
        #...##..G..............#########
        #...#...........###..E.#########
        #.G.............###...##########
        #................###############
        ##.........E.....###############
        ###.#..............#############
        ###..G........E.....############
        ###......E..........############
        ###......#....#E#...############
        ###....####.#...##.#############
        ################################
        """
    )

    def test_part1(self):
        self.assertEqual(27730, day15.part01.run(self.a))
        self.assertEqual(36334, day15.part01.run(self.b))
        self.assertEqual(39514, day15.part01.run(self.c))
        self.assertEqual(27755, day15.part01.run(self.d))
        self.assertEqual(28944, day15.part01.run(self.e))
        self.assertEqual(18740, day15.part01.run(self.f))

    def test_part3(self):
        self.assertEqual(191216, day15.part01.run(self.actual))

    def test_part2(self):
        self.assertEqual(4988, day15.part02.run(self.a))
        self.assertEqual(31284, day15.part02.run(self.c))
        self.assertEqual(3478, day15.part02.run(self.d))
        self.assertEqual(6474, day15.part02.run(self.e))
        self.assertEqual(1140, day15.part02.run(self.f))

    def test_part4(self):
        # 1550 HP and 31 rounds
        self.assertEqual(48050, day15.part02.run(self.actual))


if __name__ == '__main__':
    unittest.main()
