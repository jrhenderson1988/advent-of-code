import textwrap
import unittest

import day19.part01


class D19Tests(unittest.TestCase):
    a = textwrap.dedent(
        """
        #ip 0
        seti 5 0 1
        seti 6 0 2
        addi 0 1 0
        addr 1 2 3
        setr 1 0 0
        seti 8 0 4
        seti 9 0 5
        """
    )

    def test_part1(self):
        self.assertEqual(5, day19.part01.run(self.a))

    def test_part2(self):
        # No tests given in puzzle - reverse engineering exercise
        pass


if __name__ == '__main__':
    unittest.main()
