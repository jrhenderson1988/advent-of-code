mod assembunny;

use std::path::Path;
use crate::utils::Answers;
use std::fs::read_to_string;
use crate::day12::assembunny::instruction::Instruction;
use crate::day12::assembunny::computer::Computer;

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let content = read_to_string(path).unwrap();
    let instructions = content.lines()
        .map(|line| line.parse::<Instruction>().unwrap())
        .collect::<Vec<Instruction>>();

    let mut part1 = Computer::new(vec!['a', 'b', 'c', 'd']);
    part1.execute(&instructions);
    let part1_answer = part1.get_register_value('a');

    let mut part2 = Computer::new(vec!['a', 'b', 'c', 'd']);
    part2.set_register_value('c', 1);
    part2.execute(&instructions);
    let part2_answer = part2.get_register_value('a');

    Ok(Answers {
        part1: part1_answer.unwrap().to_string(),
        part2: part2_answer.unwrap().to_string(),
    })
}
