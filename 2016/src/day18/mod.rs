mod room;

use std::path::Path;
use crate::utils::Answers;
use std::fs::read_to_string;
use crate::day18::room::Room;

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let input = read_to_string(path).unwrap().trim().to_string();
    let room: Room = input.parse().unwrap();

    Ok(Answers {
        part1: room.safe_tiles_in_rows(40).to_string(),
        part2: room.safe_tiles_in_rows(400000).to_string(),
    })
}
