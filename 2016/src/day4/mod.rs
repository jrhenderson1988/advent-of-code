mod room;

use crate::utils::Answers;
use std::path::Path;
use std::fs::read_to_string;
use crate::day4::room::Room;

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let content = read_to_string(path).unwrap();
    let rooms = content
        .lines()
        .into_iter()
        .map(|line| line.parse::<Room>().unwrap())
        .collect::<Vec<Room>>();

    Ok(Answers {
        part1: find_total_sector_value_of_real_rooms(&rooms).to_string(),
        part2: match find_north_pole_object_storage_room(&rooms) {
            Some(room) => room.get_sector().to_string(),
            None => "Could not find matching room".to_string()
        },
    })
}

fn find_north_pole_object_storage_room(rooms: &Vec<Room>) -> Option<&Room> {
    rooms
        .into_iter()
        .filter(|room| room.is_real())
        .find(|room| {
            let name = room.decrypt_name();
            name.contains("north") && name.contains("pole") && name.contains("object")
        })
}

fn find_total_sector_value_of_real_rooms(rooms: &Vec<Room>) -> u32 {
    rooms
        .into_iter()
        .filter(|room| room.is_real())
        .map(|room| room.get_sector())
        .fold(0, |acc, value| acc + value)
}