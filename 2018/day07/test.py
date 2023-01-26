import textwrap
import unittest

import day07.part01
import day07.part02


class D07Tests(unittest.TestCase):
    a = textwrap.dedent(
        """
        Step C must be finished before step A can begin.
        Step C must be finished before step F can begin.
        Step A must be finished before step B can begin.
        Step A must be finished before step D can begin.
        Step B must be finished before step E can begin.
        Step D must be finished before step E can begin.
        Step F must be finished before step E can begin.
        """
    )

    def test_part1(self):
        self.assertEqual("CABDFE", day07.part01.run(self.a))

    def test_part2(self):
        self.assertEqual(15, day07.part02.run(self.a, workers=2, offset=0))


if __name__ == '__main__':
    unittest.main()
