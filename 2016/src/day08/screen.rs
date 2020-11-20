use std::fmt::{Display, Formatter};
use std::str::FromStr;
use regex::{Regex, Captures};

#[derive(Debug)]
pub enum Instruction {
    Rect { width: u32, height: u32 },
    RotateRow { y: usize, by: u32 },
    RotateColumn { x: usize, by: u32 },
}

impl FromStr for Instruction {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        if s.starts_with("rect") {
            let regex = Regex::new(r"^rect (\d+)x(\d+)\s*$").unwrap();
            let captures: Captures = regex.captures(s).unwrap();
            let width = captures.get(1).unwrap().as_str().parse::<u32>().unwrap();
            let height = captures.get(2).unwrap().as_str().parse::<u32>().unwrap();
            Ok(Instruction::Rect { width, height })
        } else if s.starts_with("rotate row") {
            let regex = Regex::new(r"^rotate row y=(\d+) by (\d+)\s*$").unwrap();
            let captures: Captures = regex.captures(s).unwrap();
            let y = captures.get(1).unwrap().as_str().parse::<u32>().unwrap() as usize;
            let by = captures.get(2).unwrap().as_str().parse::<u32>().unwrap();
            Ok(Instruction::RotateRow { y, by })
        } else if s.starts_with("rotate column") {
            let regex = Regex::new(r"^rotate column x=(\d+) by (\d+)\s*$").unwrap();
            let captures: Captures = regex.captures(s).unwrap();
            let x = captures.get(1).unwrap().as_str().parse::<u32>().unwrap() as usize;
            let by = captures.get(2).unwrap().as_str().parse::<u32>().unwrap();
            Ok(Instruction::RotateColumn { x, by })
        } else {
            Err(format!("Invalid instruction '{}'", s))
        }
    }
}

pub struct Screen {
    pixels: Vec<Vec<bool>>
}

impl Screen {
    pub fn new(width: usize, height: usize) -> Self {
        let pixels = vec![vec![false; width]; height];

        Screen { pixels }
    }

    pub fn execute_instructions(&mut self, instructions: &Vec<Instruction>) {
        instructions
            .iter()
            .for_each(|instruction| self.execute_instruction(&instruction));
    }

    pub fn execute_instruction(&mut self, instruction: &Instruction) {
        match instruction {
            Instruction::Rect { width, height } => self.rect(&width, &height),
            Instruction::RotateRow { y, by } => self.rotate_row(&y, &by),
            Instruction::RotateColumn { x, by } => self.rotate_column(&x, &by),
        }
    }

    pub fn total_on_pixels(&self) -> u32 {
        self.pixels.iter().fold(0, |acc, row| {
            acc + row.iter().fold(0, |acc, cell| {
                acc + match cell {
                    true => 1,
                    false => 0
                }
            })
        })
    }

    fn rect(&mut self, width: &u32, height: &u32) {
        for y in 0..*height {
            for x in 0..*width {
                self.pixels[y as usize][x as usize] = true;
            }
        }
    }

    fn rotate_row(&mut self, y: &usize, by: &u32) {
        let y = *y;
        let by = *by as usize;
        let new_row: Vec<bool> = self.rotate(self.pixels.get(y).unwrap(), &by);
        let row = self.pixels.get_mut(y).unwrap();
        row.splice(.., new_row);
    }

    fn rotate_column(&mut self, x: &usize, by: &u32) {
        let mut column = self.pixels
            .iter()
            .map(|row| row.get(*x).unwrap())
            .cloned()
            .collect::<Vec<bool>>();

        column = self.rotate(&column, &(*by as usize));

        self.pixels
            .iter_mut()
            .enumerate()
            .for_each(|(i, row)| {
                row[*x] = column[i];
            });
    }

    fn rotate(&self, items: &Vec<bool>, by: &usize) -> Vec<bool> {
        let size = items.len();
        let by = *by % size;
        let index = size - by;
        let end = items.iter()
            .skip(index)
            .take(by)
            .cloned()
            .collect::<Vec<bool>>();

        end.iter()
            .chain(items.iter().take(index))
            .cloned()
            .collect()
    }
}

impl Display for Screen {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        let output = self.pixels
            .iter()
            .map(|row| {
                row
                    .iter()
                    .map(|cell| {
                        if *cell == true {
                            "#".to_string()
                        } else {
                            ".".to_string()
                        }
                    })
                    .collect::<Vec<String>>()
                    .join(" ")
            })
            .collect::<Vec<String>>()
            .join("\n");

        write!(f, "\n{}\n", output)
    }
}