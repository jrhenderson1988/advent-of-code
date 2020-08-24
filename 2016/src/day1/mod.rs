mod state;
mod instruction;
mod turn;

use std::path::Path;
use std::fs::read_to_string;
use crate::day1::state::State;
use crate::day1::instruction::Instruction;
use crate::utils::{Answers, Direction};

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let contents = read_to_string(path).unwrap();
    let instructions: Vec<Instruction> = contents.split(',')
        .into_iter()
        .map(|i| Instruction::from(i))
        .collect();

    let mut state = State::new(0, 0, Direction::North);
    state.follow_instructions(&instructions);

    Ok(Answers {
        part1: state.distance_from(&0, &0).to_string(),
        part2: match state.visited_first_twice {
            Some(_) => state.distance_to_first_visited_from(&0, &0).to_string(),
            None => "None...".to_string()
        }
    })
}
