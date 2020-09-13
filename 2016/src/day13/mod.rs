mod maze;

use std::path::Path;
use crate::utils::Answers;
use std::fs::read_to_string;
use crate::day13::maze::Maze;

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let content = read_to_string(path).unwrap();
    let favourite_number = content.trim().parse::<u32>().unwrap();
    let mut maze = Maze::new(favourite_number);

    Ok(Answers {
        part1: maze.minimum_steps_to((1, 1), (31, 39)).to_string(),
        part2: maze.total_reachable_within_50_steps((1, 1)).to_string(),
    })
}
