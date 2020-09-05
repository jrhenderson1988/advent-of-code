mod items;
mod building;

use std::path::Path;
use crate::utils::Answers;
use crate::day11::building::{Floor, Building};
use std::collections::BTreeSet;
use crate::day11::items::Item::{Microchip, Generator};

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let mut first = BTreeSet::new();
    first.insert(Microchip("promethium".to_string()));
    first.insert(Generator("promethium".to_string()));
    let mut second = BTreeSet::new();
    second.insert(Generator("cobalt".to_string()));
    second.insert(Generator("curium".to_string()));
    second.insert(Generator("ruthenium".to_string()));
    second.insert(Generator("plutonium".to_string()));
    let mut third = BTreeSet::new();
    third.insert(Microchip("cobalt".to_string()));
    third.insert(Microchip("curium".to_string()));
    third.insert(Microchip("ruthenium".to_string()));
    third.insert(Microchip("plutonium".to_string()));
    let fourth = BTreeSet::new();

    let building = Building::new(0, vec![
        Floor::new(first),
        Floor::new(second),
        Floor::new(third),
        Floor::new(fourth),
    ], 3);

    Ok(Answers {
        part1: building.minimum_number_of_steps().to_string(),
        part2: "TODO".to_string(),
    })
}
