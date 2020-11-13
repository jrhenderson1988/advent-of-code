use std::str::FromStr;
use std::cmp::Ordering::{Less, Greater, Equal};
use std::cmp::max;

#[derive(Debug, Copy, Clone)]
pub struct IPRange {
    pub from: u32,
    pub to: u32,
}

impl IPRange {
    pub fn new(from: u32, to: u32) -> Self {
        Self { from, to }
    }

    pub fn contains(&self, address: u32) -> bool {
        address >= self.from && address <= self.to
    }
}

impl FromStr for IPRange {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let parts: Vec<String> = s.split("-").map(|p| p.trim().to_string()).collect();
        if parts.len() != 2 {
            return Err("unexpected number of parts".to_string());
        }

        let from: u32 = parts.get(0).unwrap().parse().unwrap();
        let to: u32 = parts.get(1).unwrap().parse().unwrap();
        Ok(Self::new(from, to))
    }
}

#[derive(Debug, Clone)]
pub struct Firewall {
    blocked_ranges: Vec<IPRange>
}

impl Firewall {
    pub fn new(blocked_ranges: Vec<IPRange>) -> Self {
        Self { blocked_ranges }
    }

    pub fn lowest_allowed_address(&self) -> u32 {
        let ranges = self.condense_ranges();
        let mut address = 0u32;
        let mut it = ranges.iter();
        loop {
            let next = it.next();
            if next.is_none() {
                return address;
            }

            let current_range = next.unwrap();
            if current_range.to < address {
                continue;
            }

            if !current_range.contains(address) {
                return address;
            }

            address = current_range.to + 1;
        }
    }

    pub fn total_allowed_addresses(&self) -> u32 {
        let mut ranges = self.condense_ranges();
        ranges.insert(0, IPRange::new(0, 0));
        ranges.push(IPRange::new(std::u32::MAX, std::u32::MAX));

        let mut count = 0;
        (1..ranges.len())
            .for_each(|i| {
                let a = ranges.get(i - 1).unwrap();
                let b = ranges.get(i).unwrap();
                let diff = b.from - a.to;

                if diff > 0 {
                    count += diff - 1;
                }
            });

        count
    }

    fn condense_ranges(&self) -> Vec<IPRange> {
        let mut ranges = self.blocked_ranges.to_vec();

        ranges.sort_by(|a, b|
            if a.from < b.from {
                Less
            } else if a.from > b.from {
                Greater
            } else {
                Equal
            }
        );

        let mut condensed: Vec<IPRange> = vec![];
        for range in ranges {
            if let Some(mut end) = condensed.pop() {
                let upper = if end.to == std::u32::MAX { end.to } else { end.to + 1 };
                if range.from >= end.from && range.from <= upper {
                    end.to = max(end.to, range.to);
                    condensed.push(end);
                } else {
                    condensed.push(end);
                    condensed.push(range);
                }
            } else {
                condensed.push(range.clone());
            }
        }

        condensed
    }
}

impl FromStr for Firewall {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        Ok(Self::new(s.lines().map(|line| line.trim().parse::<IPRange>().unwrap()).collect()))
    }
}
