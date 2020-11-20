mod compressed_file;

use std::path::Path;
use crate::utils::Answers;
use std::fs::read_to_string;
use crate::day09::compressed_file::CompressedFile;

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let content = read_to_string(path).unwrap();

    Ok(Answers {
        part1: content.parse::<CompressedFile>().unwrap().calculate_decompressed_length().to_string(),
        part2: content.parse::<CompressedFile>().unwrap().calculate_decompressed_v2_length().to_string(),
    })
}
