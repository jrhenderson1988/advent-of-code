use std::path::Path;
use crate::utils::Answers;
use std::fs::read_to_string;
use regex::{Regex, Captures};

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let content = read_to_string(path).unwrap();
    let parser = Parser::new();

    let combinations_part_1 = parser.parse_combinations_as_rows(&content);
    let combinations_part_2 = parser.parse_vertically(&content);

    Ok(Answers {
        part1: total_possible_triangles(&combinations_part_1).to_string(),
        part2: total_possible_triangles(&combinations_part_2).to_string(),
    })
}

struct Parser {
    pattern: Regex
}

impl Parser {
    pub fn new() -> Self {
        let pattern = Regex::new(r"^\s*(\d+)\s+(\d+)\s+(\d+)\s*$").unwrap();
        Parser { pattern }
    }

    pub fn parse_combinations_as_rows(&self, input: &str) -> Vec<(u32, u32, u32)> {
        input
            .lines()
            .into_iter()
            .map(|line| {
                let captures: Captures = self.pattern.captures(line).unwrap();
                (
                    captures[1].parse::<u32>().unwrap(),
                    captures[2].parse::<u32>().unwrap(),
                    captures[3].parse::<u32>().unwrap()
                )
            })
            .collect::<Vec<(u32, u32, u32)>>()
    }

    pub fn parse_vertically(&self, input: &str) -> Vec<(u32, u32, u32)> {
        let mut items = vec![];
        items.extend(self.parse_column(input, 1));
        items.extend(self.parse_column(input, 2));
        items.extend(self.parse_column(input, 3));

        items
            .chunks_exact(3)
            .into_iter()
            .map(|chunk| (chunk[0], chunk[1], chunk[2]))
            .collect()
    }

    fn parse_column(&self, input: &str, column: u32) -> Vec<u32> {
        input
            .lines()
            .into_iter()
            .map(|line|
                self.pattern
                    .captures(line)
                    .unwrap()[column as usize]
                    .parse::<u32>()
                    .unwrap()
            )
            .collect()
    }
}

fn total_possible_triangles(lines: &Vec<(u32, u32, u32)>) -> u32 {
    lines
        .into_iter()
        .filter(|lengths| {
            lengths.0 < (lengths.1 + lengths.2) &&
                lengths.1 < (lengths.0 + lengths.2) &&
                lengths.2 < (lengths.0 + lengths.1)
        })
        .count() as u32
}
