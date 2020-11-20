mod keypad;
mod direction;

use std::path::Path;
use std::fs::read_to_string;
use crate::utils::Answers;
use crate::day02::keypad::Keypad;
use crate::day02::direction::Direction;

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let content = read_to_string(path).unwrap();
    let instructions: Vec<Vec<Direction>> = content.lines()
        .into_iter()
        .map(|line|
            line.chars()
                .into_iter()
                .map(|ch| ch.to_string().parse::<Direction>().unwrap())
                .collect::<Vec<Direction>>())
        .collect();

    let layout1 = vec![
        vec!['1', '2', '3'],
        vec!['4', '5', '6'],
        vec!['7', '8', '9'],
    ];
    let mut keypad1 = Keypad::new(&layout1, '5');

    let layout2 = vec![
        vec![' ', ' ', '1', ' ', ' '],
        vec![' ', '2', '3', '4', ' '],
        vec!['5', '6', '7', '8', '9'],
        vec![' ', 'A', 'B', 'C', ' '],
        vec![' ', ' ', 'D', ' ', ' '],
    ];
    let mut keypad2 = Keypad::new(&layout2, '5');

    Ok(Answers {
        part1: keypad1.derive_access_code(&instructions),
        part2: keypad2.derive_access_code(&instructions)
    })
}