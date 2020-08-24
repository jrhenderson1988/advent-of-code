use std::fmt::{Formatter, Result, Display};
use crate::day1::turn::Turn;

#[derive(Debug)]
pub struct Instruction {
    pub turn: Turn,
    pub distance: u32,
}

impl Instruction {
    pub fn from(input: &str) -> Instruction {
        let (turn, distance) = input.trim().split_at(1);

        Instruction {
            turn: match turn {
                "R" => Turn::Right,
                "L" => Turn::Left,
                _ => panic!("Invalid turn {}", turn),
            },
            distance: distance.parse::<u32>().unwrap()
        }
    }
}

impl Display for Instruction {
    fn fmt(&self, f: &mut Formatter<'_>) -> Result {
        write!(f, "{}{}", self.turn, self.distance)
    }
}