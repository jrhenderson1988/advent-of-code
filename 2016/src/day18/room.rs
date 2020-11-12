use std::str::FromStr;
use crate::day18::room::Tile::{Trap, Safe};

#[derive(Debug, Copy, Clone, Eq, PartialEq)]
pub enum Tile {
    Trap,
    Safe,
}

#[derive(Debug, Clone)]
pub struct Room {
    tiles: Vec<Tile>
}

impl Room {
    pub fn new(tiles: Vec<Tile>) -> Self {
        Self { tiles }
    }

    pub fn next_row(&self, current_row: Vec<Tile>) -> Vec<Tile> {
        current_row
            .iter()
            .enumerate()
            .map(|(i, _)| {
                let (left, center, right) = self.get_comparable_tiles(&current_row, i);
                if self.is_trap(left, center, right) {
                    Trap
                } else {
                    Safe
                }
            })
            .collect()
    }

    pub fn safe_tiles_in_rows(&self, rows: usize) -> u32 {
        let mut current_row = self.tiles.clone();
        let mut safe_tiles = self.count_safe_tiles(&current_row);
        for _ in 1..rows {
            current_row = self.next_row(current_row);
            safe_tiles += self.count_safe_tiles(&current_row);
        }

        safe_tiles
    }

    fn count_safe_tiles(&self, row: &Vec<Tile>) -> u32 {
        row.iter().filter(|&t| *t == Safe).count() as u32
    }

    fn get_comparable_tiles(&self, current_row: &Vec<Tile>, index: usize) -> (Tile, Tile, Tile) {
        let left =
            if index == 0 {
                Safe
            } else {
                current_row.get(index - 1).unwrap().clone()
            };
        let center = current_row.get(index).unwrap().clone();
        let right =
            if index == current_row.len() - 1 {
                Safe
            } else {
                current_row.get(index + 1).unwrap().clone()
            };

        (left, center, right)
    }

    fn is_trap(&self, left: Tile, center: Tile, right: Tile) -> bool {
        (left == Trap && center == Trap && right == Safe)
            || (center == Trap && right == Trap && left == Safe)
            || (left == Trap && center == Safe && right == Safe)
            || (left == Safe && center == Safe && right == Trap)
    }
}

impl FromStr for Room {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let tiles: Vec<Tile> = s.chars()
            .into_iter()
            .map(|c|
                match c {
                    '.' => Safe,
                    '^' => Trap,
                    _ => panic!("unexpected input")
                }
            )
            .collect();

        Ok(Self::new(tiles))
    }
}