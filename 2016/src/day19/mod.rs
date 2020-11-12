mod circle;

use std::path::Path;
use crate::utils::Answers;
use std::fs::read_to_string;
use crate::day19::circle::Circle;

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let input = read_to_string(path).unwrap().trim().to_string();
    let total_elves: u32 = input.parse().unwrap();

    Ok(Answers {
        part1: Circle::new(total_elves).elf_with_all_the_presents_p1().to_string(),
        part2: Circle::new(total_elves).elf_with_all_the_presents_p2().to_string(),
    })
}
