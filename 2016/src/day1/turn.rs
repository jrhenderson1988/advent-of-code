use std::fmt::{Display, Formatter};
use std::str::FromStr;
use std::result::Result;

#[derive(Debug)]
pub enum Turn {
    Left,
    Right
}

impl Display for Turn {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        write!(f, "{}", match self {
            Turn::Left => "L",
            Turn::Right => "R",
        })
    }
}

impl FromStr for Turn {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        if s.to_uppercase() == "L" {
            Ok(Self::Left)
        } else if s.to_uppercase() == "R" {
            Ok(Self::Right)
        } else {
            Err(String::from("Unexpected value"))
        }
    }
}