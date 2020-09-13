use std::collections::HashMap;
use std::fmt::{Display, Formatter};
use pathfinding::directed::astar::astar;
use itertools::Itertools;

#[derive(Clone, Copy, Debug)]
pub enum Area {
    Wall,
    Open,
}

impl Display for Area {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        write!(f, "{}", match self {
            Area::Open => '.',
            Area::Wall => '#'
        })
    }
}

pub struct Maze {
    favourite_number: u32,
    discovered: HashMap<(u32, u32), Area>,
}

impl Maze {
    pub fn new(favourite_number: u32) -> Self {
        Self { favourite_number, discovered: HashMap::new() }
    }

    pub fn get_area(&mut self, x: u32, y: u32) -> Area {
        match self.discovered.get(&(x, y)) {
            Some(area) => *area,
            None => {
                let area = self.determine_area(x, y);
                self.discovered.insert((x, y), area);
                area
            }
        }
    }

    fn determine_area(&self, x: u32, y: u32) -> Area {
        let number = ((x * x) + (3 * x) + (2 * x * y) + y + (y * y)) + self.favourite_number;
        if number.count_ones() % 2 == 0 {
            Area::Open
        } else {
            Area::Wall
        }
    }

    pub fn minimum_steps_to(&mut self, from: (u32, u32), to: (u32, u32)) -> u32 {
        let result = astar(
            &from,
            |&point|
                self.get_neighbours(point)
                    .iter()
                    .map(|&other| (other, 1u32))
                    .collect::<Vec<((u32, u32), u32)>>(),
            |_| 0,
            |&other| other == to,
        );

        match result {
            Some((path, _cost)) => (path.len() - 1) as u32,
            None => 0u32
        }
    }

    pub fn total_reachable_within_50_steps(&mut self, start: (u32, u32)) -> u32 {
        let mut queue: Vec<((u32, u32), u32)> = vec![(start, 0)];
        let mut distances: HashMap<(u32, u32), u32> = HashMap::new();

        while let Some(item) = queue.pop() {
            let (point, current_distance) = item;
            distances.insert(point, current_distance);

            for neighbour in self.get_neighbours(point) {
                let distance = current_distance + 1;
                let existing_distance = distances.get(&neighbour);
                if existing_distance.is_none() || *existing_distance.unwrap() > distance {
                    queue.push((neighbour, distance));
                }
            }
        }

        distances.values()
            .filter(|&&distance| distance <= 50)
            .collect_vec()
            .len() as u32
    }

    pub fn get_neighbours(&mut self, point: (u32, u32)) -> Vec<(u32, u32)> {
        vec![(0, -1), (1, 0), (0, 1), (-1, 0)]
            .iter()
            .map(|&other| (other.0 + (point.0 as i32), other.1 + (point.1 as i32)))
            .filter(|&other| other.0 as i32 >= 0 && other.1 as i32 >= 0)
            .map(|other| (other.0 as u32, other.1 as u32))
            .filter(|&other|
                match self.get_area(other.0, other.1) {
                    Area::Open => true,
                    Area::Wall => false
                }
            )
            .collect::<Vec<(u32, u32)>>()
    }
}
