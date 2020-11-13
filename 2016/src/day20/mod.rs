mod firewall;

use std::path::Path;
use crate::utils::Answers;
use std::fs::read_to_string;
use crate::day20::firewall::Firewall;

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let input = read_to_string(path).unwrap().trim().to_string();
    let firewall: Firewall = input.parse().unwrap();

    Ok(Answers {
        part1: firewall.lowest_allowed_address().to_string(),
        part2: firewall.total_allowed_addresses().to_string(),
    })
}



