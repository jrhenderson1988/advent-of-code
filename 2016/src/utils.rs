use std::fmt::{Display, Formatter, Debug};
use std::hash::Hash;
use std::collections::{VecDeque, HashSet, HashMap};
use std::option::Option::Some;

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

pub fn bfs<T, G, N>(root: &T, is_goal: G, next_nodes: N) -> Option<Vec<T>>
    where
        T: Eq + Hash + Clone + Debug,
        G: Fn(&T) -> bool,
        N: Fn(&T) -> Vec<T>,
{
    let mut q: VecDeque<T> = VecDeque::new();
    let mut discovered: HashSet<T> = HashSet::new();
    let mut parents: HashMap<T, T> = HashMap::new();

    q.push_back(root.clone());
    discovered.insert(root.clone());
    while let Some(v) = q.pop_front() {

        if is_goal(&v) {
            println!("Found goal!");

            let mut path = vec![v.clone()];
            let mut current = v.clone();
            while let Some(parent) = parents.get(&current) {
                path.insert(0, parent.clone());
                current = parent.clone();
            }

            return Some(path);
        }

        for w in next_nodes(&v) {
            if !discovered.contains(&w) {
                discovered.insert(w.clone());
                parents.insert(w.clone(), v.clone());
                q.push_back(w.clone());
            }
        }
    }

    None
}
