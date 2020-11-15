mod program;
mod command;

use std::path::Path;
use crate::utils::Answers;
use std::fs::read_to_string;
use crate::day21::program::Program;

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let input = read_to_string(path).unwrap().trim().to_string();

    let program: Program = input.parse().unwrap();

    Ok(Answers {
        part1: program.execute("abcdefgh").to_string(),
        part2: program.execute_reverse("fbgdceah").to_string(),
    })
}



