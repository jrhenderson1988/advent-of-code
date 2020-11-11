#[derive(Debug, Copy, Clone, Eq, PartialEq, Hash)]
pub enum Direction {
    Up,
    Down,
    Left,
    Right,
}

impl Direction {
    pub fn get_char(&self) -> char {
        match self {
            Self::Up => 'U',
            Self::Down => 'D',
            Self::Left => 'L',
            Self::Right => 'R',
        }
    }

    pub fn apply(&self, from: (usize, usize)) -> (usize, usize) {
        match self {
            Self::Up => (from.0, from.1 - 1),
            Self::Down => (from.0, from.1 + 1),
            Self::Left => (from.0 - 1, from.1),
            Self::Right => (from.0 + 1, from.1),
        }
    }
}