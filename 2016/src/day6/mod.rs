use std::path::Path;
use crate::utils::Answers;
use std::fs::read_to_string;
use std::collections::HashMap;
use std::cmp::Ordering::{Less, Greater};

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let content = read_to_string(path).unwrap();

    Ok(Answers {
        part1: error_corrected_message(&content, true).to_string(),
        part2: error_corrected_message(&content, false).to_string(),
    })
}

fn build_counts_by_row(content: &str) -> HashMap<u8, HashMap<char, u32>> {
    let mut counts: HashMap<u8, HashMap<char, u32>> = HashMap::new();
    content
        .lines()
        .into_iter()
        .for_each(|line| {
            line
                .chars()
                .enumerate()
                .for_each(|(col, ch)| {
                    let col = col as u8;
                    if !counts.contains_key(&col) {
                        counts.insert(col, HashMap::new());
                    }

                    let values = counts.get_mut(&col).unwrap();
                    values.insert(ch, match values.get(&ch) {
                        Some(value) => value + 1,
                        None => 1
                    });
                })
        });

    counts
}

fn error_corrected_message(content: &str, most_common: bool) -> String {
    let counts_by_row = build_counts_by_row(content);
    let mut cols = counts_by_row.keys().cloned().collect::<Vec<u8>>();
    cols.sort();

    cols
        .into_iter()
        .map(|col| {
            let counts = counts_by_row.get(&col).unwrap();
            let mut letters = counts.keys().cloned().collect::<Vec<char>>();

            letters.sort_by(|a, b| {
                if counts.get(a).unwrap() > counts.get(b).unwrap() {
                    if most_common {
                        Less
                    } else {
                        Greater
                    }
                } else {
                    if most_common {
                        Greater
                    } else {
                        Less
                    }
                }
            });

            letters.first().unwrap().to_string()
        })
        .collect::<Vec<String>>()
        .join("")
}