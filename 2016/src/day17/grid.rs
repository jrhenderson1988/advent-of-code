use std::fmt::{Display, Formatter, Result};
use pathfinding::directed::bfs::bfs;
use itertools::Itertools;

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

#[derive(Debug, Clone, Eq, PartialEq, Hash)]
pub struct Grid {
    width: usize,
    height: usize,
    current: (usize, usize),
    target: (usize, usize),
    passcode: String,
    path: Vec<Direction>,
}

impl Grid {
    pub fn new(
        width: usize,
        height: usize,
        passcode: String,
        current: (usize, usize),
        target: (usize, usize),
        path: Vec<Direction>,
    ) -> Self {
        Self { width, height, passcode, current, target, path }
    }

    pub fn unlocked_doors(&self) -> Vec<Direction> {
        let digest = md5::compute(self.get_sequence());
        let hash = format!("{:x}", digest);
        let directions = vec![Direction::Up, Direction::Down, Direction::Left, Direction::Right];
        hash.chars()
            .take(4)
            .map(|c| vec!['b', 'c', 'd', 'e', 'f'].contains(&c))
            .enumerate()
            .filter(|(_, o)| *o)
            .map(|(i, _)| directions.get(i).unwrap().clone())
            .filter(|d| {
                match d {
                    Direction::Up => self.current.1 != 0,
                    Direction::Down => self.current.1 != self.height - 1,
                    Direction::Left => self.current.0 != 0,
                    Direction::Right => self.current.0 != self.width - 1
                }
            })
            .collect::<Vec<Direction>>()
    }

    pub fn choose_path(&self, direction: Direction) -> Self {
        let current = direction.apply(self.current);
        let path = self.path.iter().cloned().chain(vec![direction.clone()]).collect();

        Self::new(self.width, self.height, self.passcode.clone(), current, self.target, path)
    }

    pub fn possible_paths(&self) -> Vec<Self> {
        let pp = self.unlocked_doors()
            .iter()
            .map(|&d| self.choose_path(d))
            .collect();

        // println!("possible paths: {:?}", pp);

        pp
    }

    pub fn found_vault(&self) -> bool {
        self.current == self.target
    }

    pub fn get_sequence(&self) -> String {
        format!(
            "{}{}",
            self.passcode,
            self.path.iter().map(|d| d.get_char().to_string()).collect::<String>()
        )
    }

    pub fn get_path(&self) -> Vec<Direction> {
        self.path.clone()
    }

    pub fn shortest_path_to_target(&self) -> Option<Vec<Direction>> {
        let path: Vec<Self> = bfs(self, |g| g.possible_paths(), |g| g.found_vault())?;
        let last: &Grid = path.last().unwrap();
        Some(last.get_path())
    }

    pub fn printable_shortest_path_to_target(&self) -> Option<String> {
        let shortest_path = self.shortest_path_to_target()?;
        Some(shortest_path.iter().map(|d| d.get_char()).join(""))
    }
}

impl Display for Grid {
    fn fmt(&self, f: &mut Formatter<'_>) -> Result {
        write!(f, "({},{}): {}", self.current.0, self.current.1, self.get_sequence())
    }
}