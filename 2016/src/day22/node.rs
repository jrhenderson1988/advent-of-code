use std::str::FromStr;
use regex::Regex;

#[derive(Debug, Eq, PartialEq)]
pub struct Node {
    x: u8,
    y: u8,
    size: u32,
    used: u32,
    available: u32,
}

impl Node {
    pub fn new(x: u8, y: u8, size: u32, used: u32, available: u32) -> Self {
        Self { x, y, size, used, available }
    }

    pub fn is_node(&self, x: u8, y: u8) -> bool {
        self.x == x && self.y == y
    }

    pub fn is_same_node(&self, other: &Node) -> bool {
        self.is_node(other.x, other.y)
    }

    pub fn is_empty(&self) -> bool {
        self.used == 0
    }

    pub fn fits_on(&self, other: &Node) -> bool {
        self.used <= other.available
    }
}

impl FromStr for Node {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let pattern = r"/dev/grid/node-x(\d+)-y(\d+)\s+(\d+)T\s+(\d+)T\s+(\d+)T\s+\d+%";
        let pattern = Regex::new(pattern).unwrap();
        if pattern.is_match(s) {
            let group = pattern.captures(s).unwrap();
            let x: u8 = (&group[1]).to_string().parse().unwrap();
            let y: u8 = (&group[2]).to_string().parse().unwrap();
            let size: u32 = (&group[3]).to_string().parse().unwrap();
            let used: u32 = (&group[4]).to_string().parse().unwrap();
            let available: u32 = (&group[5]).to_string().parse().unwrap();
            Ok(Self::new(x, y, size, used, available))
        } else {
            Err("Nope".to_string())
        }
    }
}

#[cfg(test)]
mod test {
    use crate::day22::node::Node;

    #[test]
    fn from_str() {
        assert_eq!(
            Node { x: 0, y: 0, size: 93, used: 71, available: 22 },
            "/dev/grid/node-x0-y0     93T   71T    22T   76%".parse::<Node>().unwrap()
        );
        assert_eq!(
            Node { x: 8, y: 17, size: 93, used: 68, available: 25 },
            "/dev/grid/node-x8-y17    93T   68T    25T   73%".parse::<Node>().unwrap()
        )
    }
}