import textwrap
import unittest

import day18.part01
import day18.part02


class D18Tests(unittest.TestCase):
    a = textwrap.dedent(
        """
        .#.#...|#.
        .....#|##|
        .|..|...#.
        ..|#.....#
        #.#|||#|#|
        ...#.||...
        .|....|...
        ||...#|.#|
        |.||||..|.
        ...#.|..|.
        """
    )

    def test_part1(self):
        self.assertEqual(1147, day18.part01.run(self.a))

    def test_part2(self):
        # No tests given in puzzle
        pass


if __name__ == '__main__':
    unittest.main()
