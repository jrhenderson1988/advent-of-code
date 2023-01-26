import textwrap
import unittest

import day01.part01
import day01.part02


class D01Tests(unittest.TestCase):
    a = textwrap.dedent(
        """
        +1
        +1
        +1
        """
    )

    b = textwrap.dedent(
        """
        +1
        +1
        -2
        """
    )

    c = textwrap.dedent(
        """
        -1
        -2
        -3
        """
    )

    d = textwrap.dedent(
        """
        +1
        -1
        """
    )

    e = textwrap.dedent(
        """
        +3
        +3
        +4
        -2
        -4
        """
    )

    f = textwrap.dedent(
        """
        -6
        +3
        +8
        +5
        -6
        """
    )

    g = textwrap.dedent(
        """
        +7
        +7
        -2
        -7
        -4
        """
    )

    def test_part1(self):
        self.assertEqual(3, day01.part01.run(self.a))
        self.assertEqual(0, day01.part01.run(self.b))
        self.assertEqual(-6, day01.part01.run(self.c))

    def test_part2(self):
        self.assertEqual(0, day01.part02.run(self.d))
        self.assertEqual(10, day01.part02.run(self.e))
        self.assertEqual(5, day01.part02.run(self.f))
        self.assertEqual(14, day01.part02.run(self.g))


if __name__ == '__main__':
    unittest.main()
