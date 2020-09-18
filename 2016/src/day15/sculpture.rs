use std::str::FromStr;
use regex::Regex;

#[derive(Debug)]
pub struct Disc {
    positions: u32,
    position: u32,
}

impl Disc {
    pub fn new(positions: u32, position: u32) -> Self {
        Self { positions, position }
    }

    pub fn position_at(&self, time: u32) -> u32 {
        (time + self.position) % self.positions
    }
}

impl FromStr for Disc {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let pattern = r"^Disc #\d+ has (\d+) positions; at time=0, it is at position (\d+)\.$";
        let regex = Regex::new(pattern).unwrap();
        let captures = regex.captures(s).unwrap();
        let positions = captures.get(1).unwrap().as_str().parse::<u32>().unwrap();
        let position = captures.get(2).unwrap().as_str().parse::<u32>().unwrap();

        Ok(Self::new(positions, position))
    }
}

#[derive(Debug)]
pub struct Sculpture {
    discs: Vec<Disc>
}

impl Sculpture {
    pub fn new(discs: Vec<Disc>) -> Self {
        Self { discs }
    }

    pub fn drop_capsule_at(&self, time: u32) -> bool {
        let mut t = time;
        for disc in self.discs.iter() {
            t += 1;
            if disc.position_at(t) != 0 {
                return false;
            }
        }

        true
    }

    pub fn earliest_time_to_drop_capsule(&self) -> u32 {
        let mut time = 0u32;
        loop {
            if self.drop_capsule_at(time) {
                return time;
            }
            time += 1;
        }
    }

    pub fn add_disc(&mut self, disc: Disc) {
        self.discs.push(disc);
    }
}

impl FromStr for Sculpture {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        Ok(
            Sculpture::new(
                s.lines()
                    .map(|line| line.parse::<Disc>().unwrap())
                    .collect::<Vec<Disc>>())
        )
    }
}