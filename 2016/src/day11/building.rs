use std::collections::{HashSet, BTreeSet};
use std::hash::Hash;
use crate::utils::bfs;
use crate::day11::items::Item;

#[derive(Eq, PartialEq, Clone, Debug, Hash)]
pub struct Floor {
    items: BTreeSet<Item>
}

impl Floor {
    pub fn new(items: BTreeSet<Item>) -> Self {
        Floor { items }
    }

    pub fn is_safe(&self) -> bool {
        let microchips = self.items.iter()
            .map(|item| {
                match item {
                    Item::Microchip(name) => Some(name),
                    _ => None
                }
            })
            .filter(|opt| opt.is_some())
            .map(|opt| opt.unwrap())
            .cloned()
            .collect::<Vec<String>>();

        let generators = self.items.iter()
            .map(|item| {
                match item {
                    Item::Generator(name) => Some(name),
                    _ => None
                }
            })
            .filter(|opt| opt.is_some())
            .map(|opt| opt.unwrap())
            .cloned()
            .collect::<HashSet<String>>();

        generators.is_empty() || microchips.iter().all(|mc| generators.contains(mc))
    }
}

#[derive(Eq, PartialEq, Clone, Debug, Hash)]
pub struct Building {
    assembly_machine_floor: usize,
    current_floor: usize,
    floors: Vec<Floor>,
}

impl Building {
    pub fn new(current_floor: usize, floors: Vec<Floor>, assembly_machine_floor: usize) -> Self {
        Building { current_floor, floors, assembly_machine_floor }
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
        match bfs(&current, |n| *n == goal, |n| n.calculate_possible_next_states()) {
            Some(path) => (path.len() as u32) - 1, // path includes initial state
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
            for combination in self.possible_item_combinations() {
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

    pub fn possible_item_combinations(&self) -> Vec<Vec<Item>> {
        let mut combinations: Vec<Vec<Item>> = vec![];
        let floor = self.floors.get(self.current_floor).unwrap();
        let total_items = floor.items.len();

        let items = floor.items.iter().cloned().collect::<Vec<Item>>();
        for i in 0..total_items {
            combinations.push(vec![items.get(i).unwrap().clone()]);
            for j in i + 1..total_items {
                combinations.push(
                    vec![
                        items.get(i).unwrap().clone(),
                        items.get(j).unwrap().clone()
                    ],
                );
            }
        }

        combinations
    }
}

















// #[derive(Eq, PartialEq, Clone, Debug, Hash)]
// pub struct Floor {
//     microchips: Vec<String>,
//     generators: Vec<String>,
// }
//
// impl Floor {
//     pub fn new(microchips: Vec<String>, generators: Vec<String>) -> Self {
//         Floor { microchips, generators }
//     }
//
//     pub fn is_safe(&self) -> bool {
//         self.microchips.iter().all(|mc| self.generators.is_empty() || self.generators.contains(mc))
//     }
// }
//
// #[derive(Eq, PartialEq, Clone, Debug, Hash)]
// pub struct Building {
//     assembly_machine_floor: usize,
//     current_floor: usize,
//     floors: Vec<Floor>,
// }
//
// impl Building {
//     pub fn new(current_floor: usize, floors: Vec<Floor>, assembly_machine_floor: usize) -> Self {
//         Building { current_floor, floors, assembly_machine_floor }
//     }
//
//     pub fn is_safe(&self) -> bool {
//         self.floors.iter().all(|floor| floor.is_safe())
//     }
//
//     pub fn calculate_hash(&self) -> u64 {
//         let mut s = DefaultHasher::new();
//         self.hash(&mut s);
//         s.finish()
//     }
//
//     pub fn can_move_up(&self) -> bool {
//         self.current_floor < self.floors.len() - 1
//     }
//
//     pub fn can_move_down(&self) -> bool {
//         self.current_floor > 0
//     }
//
//     pub fn minimum_number_of_steps(&self) -> u32 {
//         let current = self.clone();
//         let goal = self.calculate_goal();
//         match bfs(&current, |n| *n == goal, |n| n.calculate_possible_next_states()) {
//             Some(path) => path.len() as u32,
//             None => 0
//         }
//     }
//
//     pub fn calculate_goal(&self) -> Building {
//         let current_floor = self.assembly_machine_floor;
//         let floors = (0..self.floors.len())
//             .map(|i| {
//                 if i == self.assembly_machine_floor {
//                     Floor::new(
//                         self.floors.iter()
//                             .cloned()
//                             .flat_map(|f| f.microchips)
//                             .collect::<Vec<String>>(),
//                         self.floors.iter()
//                             .cloned()
//                             .flat_map(|f| f.generators)
//                             .collect::<Vec<String>>(),
//                     )
//                 } else {
//                     Floor::new(vec![], vec![])
//                 }
//             })
//             .collect::<Vec<Floor>>();
//
//         Building { current_floor, floors, assembly_machine_floor: self.assembly_machine_floor }
//     }
//
//     pub fn calculate_possible_next_states(&self) -> Vec<Self> {
//         let mut states: Vec<Self> = vec![];
//         let current_floor = self.floors.get(self.current_floor);
//         let possible_next_floors = self.possible_next_floors();
//         // Can pick up one object
//         // Can pick up two objects
//         // Can go up and can go down
//
//         states
//     }
//
//     fn possible_next_floors(&self) -> Vec<usize> {
//         let mut floors = vec![];
//
//         if self.can_move_up() {
//             floors.push(self.current_floor + 1);
//         }
//
//         if self.can_move_down() {
//             floors.push(self.current_floor - 1);
//         }
//
//         floors
//     }
// }
