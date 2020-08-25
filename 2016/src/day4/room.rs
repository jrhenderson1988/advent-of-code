use std::str::FromStr;
use regex::{Regex, Match};
use std::collections::HashMap;
use std::cmp::Ordering::{Less, Greater};

static ASCII_LOWER: [char; 26] = [
    'a', 'b', 'c', 'd', 'e',
    'f', 'g', 'h', 'i', 'j',
    'k', 'l', 'm', 'n', 'o',
    'p', 'q', 'r', 's', 't',
    'u', 'v', 'w', 'x', 'y',
    'z',
];

#[derive(Debug)]
pub struct Room {
    name: String,
    sector: u32,
    checksum: String,
}

impl Room {
    pub fn new(name: String, sector: u32, checksum: String) -> Self {
        Room { name, sector, checksum }
    }

    pub fn get_sector(&self) -> u32 {
        self.sector
    }

    pub fn is_real(&self) -> bool {
        self.calculate_checksum() == self.checksum
    }

    pub fn calculate_checksum(&self) -> String {
        let mut counts: HashMap<char, u32> = HashMap::new();
        let mut order: Vec<char> = vec![];
        for ch in self.name.chars() {
            if ch == '-' {
                continue;
            }

            if !order.contains(&ch) {
                order.push(ch);
            }

            let existing = counts.get(&ch);
            counts.insert(ch, match existing {
                Some(count) => count + 1,
                None => 1
            });
        }

        let mut keys: Vec<char> = counts.keys().cloned().collect();
        keys.sort_by(|a, b| {
            let a_count = counts.get(a).unwrap();
            let b_count = counts.get(b).unwrap();
            if a_count < b_count {
                Greater
            } else if a_count > b_count {
                Less
            } else if a < b {
                Less
            } else {
                Greater
            }
        });

        keys.into_iter()
            .map(|ch| ch.to_string())
            .take(5)
            .collect::<Vec<String>>()
            .join("")
    }

    pub fn decrypt_name(&self) -> String {
        let mut decrypted = String::new();
        for ch in self.name.chars() {
            decrypted.push(self.shift_char(ch));
        }

        decrypted
    }

    fn shift_char(&self, ch: char) -> char {
        if ch == '-' {
            ' '
        } else {
            let alphabet_length = ASCII_LOWER.len();
            let shift = self.sector % (alphabet_length as u32);

            // TODO - Continue here
            '?'
        }
    }
}

impl FromStr for Room {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let pattern = Regex::new(r"^([a-z\-]+)-(\d+)\[([a-z]+)]$").unwrap();
        let captures = pattern.captures(s).unwrap();
        let name: String = (&captures[1]).to_string();
        let sector: u32 = (&captures[2]).parse::<u32>().unwrap();
        let checksum: String = (&captures[3]).to_string();

        Ok(Room { name, sector, checksum })
    }
}