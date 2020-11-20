use std::collections::HashMap;
use crate::day02::direction::Direction;

pub struct Keypad {
    buttons: HashMap<(u32, u32), char>,
    current_coord: (u32, u32),
}

impl Keypad {
    pub fn new(layout: &Vec<Vec<char>>, start: char) -> Self {
        let mut buttons: HashMap<(u32, u32), char> = HashMap::new();
        let mut current_coord = (0, 0);
        for (y, row) in layout.iter().enumerate() {
            for (x, ch) in row.iter().enumerate() {
                if *ch != ' ' {
                    let coord = (x as u32, y as u32);
                    buttons.insert(coord, *ch);
                    if *ch == start {
                        current_coord = coord;
                    }
                }
            }
        }

        Keypad { buttons, current_coord }
    }

    pub fn derive_access_code(&mut self, instructions: &Vec<Vec<Direction>>) -> String {
        let numbers: Vec<String> = instructions
            .into_iter()
            .map(|directions| {
                directions
                    .into_iter()
                    .for_each(
                        |direction| self.move_direction(&direction)
                    );

                self.current_button().to_string()
            })
            .collect();

        numbers.join("")
    }

    fn move_direction(&mut self, direction: &Direction) {
        let next_coord = self.apply_delta(&self.current_coord, &direction.delta());
        if self.button_exists_at(&next_coord) {
            self.current_coord = (next_coord.0 as u32, next_coord.1 as u32);
        }
    }

    pub fn current_button(&self) -> char {
        self.buttons[&self.current_coord]
    }

    fn apply_delta(&self, coord: &(u32, u32), delta: &(i32, i32)) -> (i32, i32) {
        (coord.0 as i32 + delta.0, coord.1 as i32 + delta.1)
    }

    fn button_exists_at(&self, coord: &(i32, i32)) -> bool {
        if coord.0 < 0 || coord.1 < 0 {
            false
        } else {
            self.buttons.contains_key(&(coord.0 as u32, coord.1 as u32))
        }
    }
}
