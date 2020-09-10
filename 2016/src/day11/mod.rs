mod items;
mod building;

use std::path::Path;
use crate::utils::Answers;
use crate::day11::building::Building;
use crate::day11::items::Item::{Microchip, Generator};
use std::fs::read_to_string;

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let content = read_to_string(path).unwrap();

    println!("Hang tight, this may take a while ¯\\_(ツ)_/¯");
    println!("Building with --release is **strongly** recommended before running this one.");
    let building = content.parse::<Building>().unwrap();

    let mut building2 = building.clone();
    building2.add_item_to_floor(0, Generator("elerium".to_string()));
    building2.add_item_to_floor(0, Microchip("elerium".to_string()));
    building2.add_item_to_floor(0, Generator("dilithium".to_string()));
    building2.add_item_to_floor(0, Microchip("dilithium".to_string()));

    Ok(Answers {
        part1: building.minimum_number_of_steps().to_string(),
        part2: building2.minimum_number_of_steps().to_string(),
    })
}
