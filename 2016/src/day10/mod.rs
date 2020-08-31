mod instruction;
mod simulation;

use std::path::Path;
use crate::utils::Answers;
use std::fs::read_to_string;
use crate::day10::instruction::Instruction;
use crate::day10::simulation::Simulation;

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let content = read_to_string(path).unwrap();
    let instructions = content.lines()
        .map(|line| line.parse::<Instruction>().unwrap())
        .collect::<Vec<Instruction>>();

    Ok(Answers {
        part1: Simulation::new(&instructions).find_bot_that_compares(61, 17).unwrap().to_string(),
        part2: Simulation::new(&instructions).multiply_values_in_outputs(vec![0, 1, 2]).to_string(),
    })
}
