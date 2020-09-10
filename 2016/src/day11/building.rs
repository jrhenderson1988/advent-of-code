extern crate pathfinding;

use std::collections::{HashSet, BTreeSet};
use std::hash::Hash;
use crate::day11::items::Item;
use pathfinding::prelude::astar;
use itertools::Itertools;
use std::fmt::{Display, Formatter};
use std::str::FromStr;
use regex::Regex;


#[derive(Eq, PartialEq, Clone, Debug, Hash)]
pub struct Floor {
    items: BTreeSet<Item>
}

impl Floor {
    pub fn new(items: BTreeSet<Item>) -> Self {
        Floor { items }
    }

    pub fn is_safe(&self) -> bool {
        let mut microchips: HashSet<String> = HashSet::new();
        let mut generators: HashSet<String> = HashSet::new();
        for item in self.items.iter() {
            match item {
                Item::Microchip(name) => microchips.insert(name.clone()),
                Item::Generator(name) => generators.insert(name.clone())
            };
        }

        generators.is_empty() || microchips.iter().all(|mc| generators.contains(mc))
    }
}

impl FromStr for Floor {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        if s.contains("nothing relevant") {
            Ok(Floor::new(BTreeSet::new()))
        } else {
            let regex = Regex::new(r"(?m)([a-z]+(?:-compatible microchip| generator))").unwrap();
            let mut items: BTreeSet<Item> = BTreeSet::new();
            for m in regex.find_iter(s) {
                let item_string = m.as_str();
                items.insert(
                    if item_string.ends_with(" generator") {
                        Item::Generator(item_string.chars().take(item_string.len() - 10).collect())
                    } else {
                        Item::Microchip(item_string.chars().take(item_string.len() - 21).collect())
                    }
                );
            }

            Ok(Floor::new(items))
        }
    }
}

#[derive(Eq, PartialEq, Clone, Debug, Hash)]
pub struct Building {
    assembly_machine_floor: usize,
    current_floor: usize,
    floors: Vec<Floor>,
}

impl FromStr for Building {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let mut lines = s.lines();
        let floors = ["first", "second", "third", "fourth"]
            .iter()
            .map(|floor_str| {
                lines.find(|line| line.contains(floor_str))
                    .unwrap()
                    .parse::<Floor>()
                    .unwrap()
            })
            .collect::<Vec<Floor>>();
        let assembly_machine_floor = 3usize;
        let current_floor = 0usize;

        Ok(Building::new(current_floor, floors, assembly_machine_floor))
    }
}

impl Building {
    pub fn new(current_floor: usize, floors: Vec<Floor>, assembly_machine_floor: usize) -> Self {
        Building { current_floor, floors, assembly_machine_floor }
    }

    pub fn add_item_to_floor(&mut self, floor_index: usize, item: Item) {
        let floor = self.floors.get_mut(floor_index).unwrap();
        floor.items.insert(item);
    }

    pub fn is_safe(&self) -> bool {
        self.floors.iter().all(|floor| floor.is_safe())
    }

    pub fn can_move_up(&self) -> bool {
        self.current_floor < self.floors.len() - 1
    }

    pub fn can_move_down(&self) -> bool {
        self.current_floor > 0
    }

    pub fn minimum_number_of_steps(&self) -> u32 {
        let current = self.clone();
        let goal = self.calculate_goal();

        let result = astar(
            &current,
            move |n| {
                n.calculate_possible_next_states()
                    .into_iter()
                    .map(|node| (node.clone(), 1))
            },
            |_n| 0,
            // |n| (n.length_of_target_floor() as i32) * -10,
            |n| *n == goal,
        );

        match result {
            Some((path, _)) => (path.len() as u32) - 1,
            None => 0
        }
    }

    pub fn calculate_goal(&self) -> Building {
        let current_floor = self.assembly_machine_floor;
        let floors = (0..self.floors.len())
            .map(|i| {
                if i == self.assembly_machine_floor {
                    Floor::new(
                        self.floors.iter()
                            .cloned()
                            .flat_map(|f| f.items)
                            .collect::<BTreeSet<Item>>()
                    )
                } else {
                    Floor::new(BTreeSet::new())
                }
            })
            .collect::<Vec<Floor>>();

        Building { current_floor, floors, assembly_machine_floor: self.assembly_machine_floor }
    }

    pub fn calculate_possible_next_states(&self) -> Vec<Self> {
        let mut states: Vec<Self> = vec![];

        for next_floor in self.possible_next_floors() {
            for combination in self.possible_item_combinations(next_floor > self.current_floor) {
                let mut state = self.clone();
                let current_floor = state.floors.get_mut(self.current_floor).unwrap();
                for item in combination.clone() {
                    current_floor.items.remove(&item);
                }

                let target_floor = state.floors.get_mut(next_floor).unwrap();
                for item in combination.clone() {
                    target_floor.items.insert(item.clone());
                }

                state.current_floor = next_floor;

                if state.is_safe() {
                    states.push(state);
                }
            }
        }

        states
    }

    fn possible_next_floors(&self) -> Vec<usize> {
        let mut floors = vec![];

        if self.can_move_up() {
            floors.push(self.current_floor + 1);
        }

        if self.can_move_down() {
            floors.push(self.current_floor - 1);
        }

        floors
    }

    fn possible_item_combinations(&self, up: bool) -> Vec<Vec<Item>> {
        let total_items = self.floors.get(self.current_floor).unwrap().items.len();
        if false && up && total_items > 1 {
            self.possible_double_item_combinations()
        } else {
            let mut items = self.possible_double_item_combinations();
            items.extend(self.possible_single_item_combinations().iter().cloned());
            items
        }
    }

    fn possible_single_item_combinations(&self) -> Vec<Vec<Item>> {
        self.floors.get(self.current_floor)
            .unwrap()
            .items
            .iter()
            .cloned()
            .map(|item| vec![item])
            .collect()
    }

    fn possible_double_item_combinations(&self) -> Vec<Vec<Item>> {
        let mut combinations: Vec<Vec<Item>> = vec![];
        let floor = self.floors.get(self.current_floor).unwrap();
        let total_items = floor.items.len();
        let items = floor.items.iter().cloned().collect::<Vec<Item>>();

        for i in 0..total_items {
            for j in i + 1..total_items {
                let item_i = items.get(i).unwrap();
                let item_j = items.get(j).unwrap();
                if item_i.safe_to_move_with(item_j) {
                    combinations.push(vec![item_i.clone(), item_j.clone()]);
                }
            }
        }

        combinations
    }
}

impl Display for Building {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        write!(
            f,
            "{}",
            self.floors.iter()
                .enumerate()
                .rev()
                .map(|(idx, floor)| {
                    format!(
                        "{} | {} |",
                        if self.current_floor == idx {
                            "*"
                        } else {
                            " "
                        },
                        floor.items.iter()
                            .map(|item| {
                                match item {
                                    Item::Microchip(name) => format!("[MC-{}]", name),
                                    Item::Generator(name) => format!("[GN-{}]", name)
                                }
                            })
                            .join(" ")
                    )
                })
                .join("\n")
        )
    }
}
