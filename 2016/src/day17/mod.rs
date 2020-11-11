mod direction;
mod grid;

use std::path::Path;
use crate::utils::Answers;
use std::fs::read_to_string;
use crate::day17::grid::Grid;

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let passcode = read_to_string(path).unwrap().trim().to_string();
    let grid = Grid::new(4, 4, passcode, (0, 0), (3, 3), vec![]);

    Ok(Answers {
        part1: grid.printable_shortest_path_to_target().unwrap().to_string(),
        part2: grid.longest_path_to_target().unwrap().len().to_string(),
    })
}
