mod room;

use crate::utils::Answers;
use std::path::Path;
use std::fs::read_to_string;
use crate::day4::room::Room;

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let content = read_to_string(path).unwrap();

    println!("{:?}", content
        .lines()
        .into_iter()
        .map(|line| line.parse::<Room>().unwrap())
        .filter(|room| room.is_real())
        .map(|room| room.decrypt_name())
        .collect::<Vec<String>>());

    Ok(Answers {
        part1: content
            .lines()
            .into_iter()
            .map(|line| line.parse::<Room>().unwrap())
            .filter(|room| room.is_real())
            .map(|room| room.get_sector())
            .fold(0, |acc, value| acc + value)
            .to_string(),
        part2: "todo".to_string(),
    })
}