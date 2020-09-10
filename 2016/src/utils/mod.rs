use std::fmt::{Display, Formatter, Debug};

#[derive(Copy, Clone, Debug)]
pub enum Direction {
    North,
    East,
    South,
    West,
}

impl Direction {
    pub fn next_clockwise(self) -> Direction {
        match self {
            Direction::North => Direction::East,
            Direction::East => Direction::South,
            Direction::South => Direction::West,
            Direction::West => Direction::North,
        }
    }

    pub fn next_anticlockwise(self) -> Direction {
        match self {
            Direction::North => Direction::West,
            Direction::East => Direction::North,
            Direction::South => Direction::East,
            Direction::West => Direction::South,
        }
    }

    pub fn delta(self) -> (i8, i8) {
        match self {
            Direction::North => (0, -1),
            Direction::East => (1, 0),
            Direction::South => (0, 1),
            Direction::West => (-1, 0),
        }
    }
}

impl Display for Direction {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        write!(f, "{}", match self {
            Direction::North => "N",
            Direction::East => "E",
            Direction::South => "S",
            Direction::West => "W",
        })
    }
}

#[derive(Debug)]
pub struct Answers {
    pub part1: String,
    pub part2: String,
}

impl Display for Answers {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        write!(f, "Answers:\n\tPart 1: {}\n\tPart 2: {}\n", self.part1, self.part2)
    }
}

