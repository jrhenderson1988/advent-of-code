import textwrap
import unittest

import day22.part01


class D22Tests(unittest.TestCase):
    INPUT = textwrap.dedent(
        """
        depth: 510
        target: 10,10
        """
    )

    def test_part1(self):
        self.assertEqual(114, day22.part01.run(self.INPUT))


    # def test_part2(self):
    #     pass


if __name__ == '__main__':
    unittest.main()
