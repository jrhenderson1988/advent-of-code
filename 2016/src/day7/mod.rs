mod ipv7;

use std::path::Path;
use crate::utils::Answers;
use crate::day7::ipv7::IPv7;
use std::fs::read_to_string;

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let content = read_to_string(path).unwrap();
    let addresses = content
        .lines()
        .into_iter()
        .map(|line| line.parse::<IPv7>().unwrap())
        .collect::<Vec<IPv7>>();

    Ok(Answers {
        part1: addresses
            .iter()
            .filter(|ipv7| ipv7.supports_tls())
            .count()
            .to_string(),
        part2: addresses
            .iter()
            .filter(|ipv7| ipv7.supports_ssl())
            .count()
            .to_string(),
    })
}