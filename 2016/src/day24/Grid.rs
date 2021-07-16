use std::collections::HashMap;
use std::str::FromStr;

use itertools::Itertools;
use pathfinding::prelude::bfs;

use crate::day24::cell::Cell;
use crate::utils::Direction;

#[derive(Debug)]
pub struct Grid {
    points: HashMap<(usize, usize), Cell>,
}

impl Grid {
    pub fn shortest_path_to_visit_all(&self, start: u8, end: Option<u8>) -> u32 {
        let numbers = self.find_all_numbers();
        let shortest_paths = self.shortest_paths(&numbers);
        let others: Vec<u8> = numbers.keys().cloned().filter(|&k| k != start).collect();

        others.iter()
            .cloned()
            .permutations(others.len())
            .map(|path|
                vec![start].iter()
                    .cloned()
                    .chain(path)
                    .chain(match end {
                        Some(end) => vec![end],
                        None => vec![]
                    })
                    .collect::<Vec<u8>>())
            .map(|path| path.iter().cloned().tuple_windows::<(_, _)>().collect::<Vec<(u8, u8)>>())
            .map(|path| path.iter().fold(0u32, |acc, &step| {
                acc + match shortest_paths.get(&step) {
                    Some(distance) => distance,
                    None => shortest_paths.get(&(step.1, step.0)).expect("No value for step")
                }
            }))
            .min()
            .unwrap()
    }

    fn find_all_numbers(&self) -> HashMap<u8, (usize, usize)> {
        self.points.iter()
            .filter(|(_, v)| v.is_a_number())
            .map(|(k, v)| (v.get_number(), k.clone()))
            .collect()
    }

    fn shortest_paths(&self, numbers: &HashMap<u8, (usize, usize)>) -> HashMap<(u8, u8), u32> {
        let mut paths = HashMap::new();
        for (number, point) in numbers.iter() {
            for (other_number, other_point) in numbers.iter() {
                if number == other_number || paths.contains_key(&(*other_number, *number)) {
                    continue;
                }

                paths.insert((*number, *other_number), self.distance_between(*point, *other_point));
            }
        }

        paths
    }

    fn distance_between(&self, a: (usize, usize), b: (usize, usize)) -> u32 {
        bfs(&a, |&n| self.open_neighbours(n), |n| *n == b)
            .expect("could not find path")
            .len() as u32 - 1
    }

    fn open_neighbours(&self, of: (usize, usize)) -> Vec<(usize, usize)> {
        vec![Direction::North, Direction::East, Direction::South, Direction::West]
            .iter()
            .map(|direction| {
                let delta = direction.delta();
                let new_x = ((of.0 as i32) + (delta.0 as i32)) as usize;
                let new_y = ((of.1 as i32) + (delta.1 as i32)) as usize;
                (new_x, new_y)
            })
            .filter(|point| {
                match self.points.get(point) {
                    Some(Cell::Space) => true,
                    Some(Cell::Number(_)) => true,
                    _ => false,
                }
            })
            .collect::<Vec<(usize, usize)>>()
    }
}

impl FromStr for Grid {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let mut points = HashMap::new();

        for (y, line) in s.lines().enumerate() {
            for (x, ch) in line.chars().enumerate() {
                let key = (x, y);
                let cell = match ch {
                    '#' => Cell::Wall,
                    '.' => Cell::Space,
                    '0'..='9' => Cell::Number(ch.to_digit(10).expect("not a number") as u8),
                    _ => panic!("Unexpected character")
                };

                points.insert(key, cell);
            }
        }

        Ok(Self { points })
    }
}