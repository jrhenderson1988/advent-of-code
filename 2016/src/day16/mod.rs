mod data_generator;
use std::path::Path;
use crate::utils::Answers;
use std::fs::read_to_string;
use crate::day16::data_generator::DataGenerator;

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let content = read_to_string(path).unwrap().trim().to_string();
    let generator = DataGenerator::new(content);

    Ok(Answers {
        part1: generator.checksum_for_length(272).to_string(),
        part2: generator.checksum_for_length(35651584).to_string(),
    })
}
