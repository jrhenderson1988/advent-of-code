use std::path::Path;
use crate::utils::Answers;
use std::fs::read_to_string;

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let content = read_to_string(path).unwrap();

    Ok(Answers {
        part1: "TODO".to_string(),
        part2: "TODO".to_string(),
    })
}
