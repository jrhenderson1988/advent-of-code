import textwrap
import unittest

import day13.part01
import day13.part02


class D13Tests(unittest.TestCase):
    a = textwrap.dedent(
        """
        /->-\\        
        |   |  /----\\
        | /-+--+-\\  |
        | | |  | v  |
        \\-+-/  \\-+--/
          \\------/  
        """
    )

    b = textwrap.dedent(
        """
        />-<\\  
        |   |  
        | /<+-\\
        | | | v
        \\>+</ |
          |   ^
          \\<->/
        """
    )

    def test_part1(self):
        self.assertEqual("7,3", day13.part01.run(self.a.lstrip()))

    def test_part2(self):
        self.assertEqual("6,4", day13.part02.run(self.b.lstrip()))


if __name__ == '__main__':
    unittest.main()
