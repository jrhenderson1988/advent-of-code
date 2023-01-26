import textwrap
import unittest

import day12.part01
import day12.part02


class D12Tests(unittest.TestCase):
    a = textwrap.dedent(
        """
        initial state: #..#.#..##......###...###
        
        ...## => #
        ..#.. => #
        .#... => #
        .#.#. => #
        .#.## => #
        .##.. => #
        .#### => #
        #.#.# => #
        #.### => #
        ##.#. => #
        ##.## => #
        ###.. => #
        ###.# => #
        ####. => #
        """
    )

    def test_part1(self):
        self.assertEqual(325, day12.part01.run(self.a))

    def test_part2(self):
        self.assertEqual(999999999374, day12.part02.run(self.a))


if __name__ == '__main__':
    unittest.main()
