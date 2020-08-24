use crate::utils::Direction;
use std::fmt::{Display, Formatter};
use crate::day1::instruction::Instruction;
use crate::day1::turn::Turn;
use std::collections::{HashSet};

pub struct State {
    x: i32,
    y: i32,
    direction: Direction,
    visited: HashSet<(i32, i32)>,
    pub visited_first_twice: Option<(i32, i32)>,
}

impl State {
    pub fn new(x: i32, y: i32, direction: Direction) -> State {
        let mut visited = HashSet::new();
        visited.insert((x, y));

        State { x, y, direction, visited, visited_first_twice: Option::None }
    }

    pub fn turn(&mut self, turn: &Turn) {
        self.direction = match turn {
            Turn::Left => self.direction.next_anticlockwise(),
            Turn::Right => self.direction.next_clockwise(),
        };
    }

    pub fn walk(&mut self, distance: &u32) {
        let delta = self.direction.delta();
        let new_x = self.x + (delta.0 as i32 * *distance as i32);
        let new_y = self.y + (delta.1 as i32 * *distance as i32);

        for x in State::range(self.x, new_x) {
            self.visit(&(x, self.y));
        }

        for y in State::range(self.y, new_y) {
            self.visit(&(self.x, y));
        }
    }

    fn visit(&mut self, coords: &(i32, i32)) {
        if self.visited.contains(coords) && self.visited_first_twice.is_none() {
            self.visited_first_twice = Some(*coords);
        }

        self.visited.insert(*coords);
        self.x = coords.0;
        self.y = coords.1;
    }

    pub fn follow_instruction(&mut self, instruction: &Instruction) {
        self.turn(&instruction.turn);
        self.walk(&instruction.distance);
    }

    pub fn follow_instructions(&mut self, instructions: &Vec<Instruction>) {
        for instruction in instructions {
            self.follow_instruction(&instruction);
        }
    }

    pub fn distance_from(&self, x: &i32, y: &i32) -> u32 {
        State::distance_between(&(self.x, self.y), &(*x, *y))
    }

    pub fn distance_to_first_visited_from(&self, x: &i32, y: &i32) -> u32 {
        match self.visited_first_twice {
            Some(coords) => State::distance_between(&coords, &(*x, *y)),
            None => 0
        }
    }

    // Wtf Rust?! Y U no allow iteration of x..y where x > y?!
    fn range(from: i32, to: i32) -> Vec<i32> {
        if from < to {
            (from + 1..to + 1).collect()
        } else {
            (to..from).rev().collect()
        }
    }

    fn distance_between(a: &(i32, i32), b: &(i32, i32)) -> u32 {
        (a.0 - b.0).abs() as u32 + (a.1 - b.1).abs() as u32
    }
}

impl Display for State {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        write!(f, "({}, {}) {}", self.x, self.y, self.direction)
    }
}
