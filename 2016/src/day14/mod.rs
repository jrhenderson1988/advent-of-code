mod key_finder;
mod hasher;

use std::path::Path;
use crate::utils::Answers;
use std::fs::read_to_string;
use crate::day14::key_finder::KeyFinder;

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let content = read_to_string(path).unwrap();
    let key_finder = KeyFinder::new(content.trim().to_string());

    Ok(Answers {
        part1: key_finder.find_index_of_nth_hash(63, 0).to_string(),
        part2: key_finder.find_index_of_nth_hash(63, 2016).to_string(),
    })
}
