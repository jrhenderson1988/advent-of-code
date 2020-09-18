mod sculpture;
use std::path::Path;
use crate::utils::Answers;
use std::fs::read_to_string;
use crate::day15::sculpture::{Disc, Sculpture};

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let content = read_to_string(path).unwrap();
    let mut sculpture = content.parse::<Sculpture>().unwrap();
    let part1 = sculpture.earliest_time_to_drop_capsule();
    sculpture.add_disc(Disc::new(11, 0));
    let part2 = sculpture.earliest_time_to_drop_capsule();

    Ok(Answers {
        part1: part1.to_string(),
        part2: part2.to_string(),
    })
}
