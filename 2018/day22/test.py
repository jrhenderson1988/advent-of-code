import textwrap
import unittest

from day22 import part01, part02


class D22Tests(unittest.TestCase):
    INPUT = textwrap.dedent(
        """
        depth: 510
        target: 10,10
        """
    )

    def test_part1(self):
        self.assertEqual(114, part01.run(self.INPUT))

    def test_part2(self):
        self.assertEqual(45, part02.run(self.INPUT))


if __name__ == '__main__':
    unittest.main()
