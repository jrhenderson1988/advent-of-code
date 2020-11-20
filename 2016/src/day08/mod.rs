use std::path::Path;
use crate::utils::Answers;
use crate::day08::screen::{Screen, Instruction};
use std::fs::read_to_string;

mod screen;

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let content = read_to_string(path).unwrap();
    let instructions = content.lines()
        .into_iter()
        .map(|line| line.parse::<Instruction>().unwrap())
        .collect::<Vec<Instruction>>();

    let mut screen = Screen::new(50, 6);
    screen.execute_instructions(&instructions);

    Ok(Answers {
        part1: screen.total_on_pixels().to_string(),
        part2: screen.to_string(),
    })
}