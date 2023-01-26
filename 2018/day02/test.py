import textwrap
import unittest

import day02.part01
import day02.part02


class D02Tests(unittest.TestCase):
    a = textwrap.dedent(
        """
        abcdef
        bababc
        abbcde
        abcccd
        aabcdd
        abcdee
        ababab
        """
    )

    b = textwrap.dedent(
        """
        abcde
        fghij
        klmno
        pqrst
        fguij
        axcye
        wvxyz
        """
    )

    def test_part1(self):
        self.assertEqual(12, day02.part01.run(self.a))

    def test_part2(self):
        self.assertEqual("fgij", day02.part02.run(self.b))


if __name__ == '__main__':
    unittest.main()
