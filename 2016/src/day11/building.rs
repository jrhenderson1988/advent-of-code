extern crate pathfinding;

use std::collections::{HashSet, BTreeSet};
use std::hash::Hash;
use crate::day11::items::Item;
use pathfinding::prelude::astar;
use itertools::Itertools;
use std::fmt::{Display, Formatter};


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

        let mut comparisons = 0u64;
        let result = astar(
            &current,
            move |n| {
                n.calculate_possible_next_states()
                    .into_iter()
                    .map(|node| (node.clone(), 1))
            },
            |n| 0,
            // |n| (n.length_of_target_floor() as i32) * -10,
            |n| {
                comparisons += 1;
                *n == goal
            },
        );

        println!("Comparisons: {}", comparisons);
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

    pub fn calculate_possible_next_states(&self/*, discovered: &HashSet<Self>*/) -> Vec<Self> {
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
                    // let mut found_equivalent_state = false;
                    // for d in discovered {
                    //     if self.equivalent_to(&d) {
                    //         found_equivalent_state = true;
                    //         break;
                    //     }
                    // }
                    //
                    // if !found_equivalent_state {
                    //     states.push(state);
                    // }
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

    // pub fn distance_from(&self, other: &Self) -> u32 {
    //     let mut table: HashMap<Item, usize> = HashMap::new();
    //     for (idx, floor) in other.floors.iter().enumerate() {
    //         for item in floor.items.iter().cloned() {
    //             table.insert(item, idx);
    //         }
    //     }
    //
    //     let mut cost = 0;
    //     for (idx, floor) in self.floors.iter().enumerate() {
    //         for item in floor.items.iter() {
    //             let target_idx = table.get(&item).unwrap();
    //             cost += ((target_idx - idx) as i32).abs() as u32
    //         }
    //     }
    //
    //     cost
    // }

    pub fn length_of_target_floor(&self) -> u32 {
        let target_floor = self.floors.get(self.assembly_machine_floor).unwrap();

        target_floor.items.len() as u32
    }

    // /// Return a set of tuples for the current state in format of:
    // /// [(microchip_a_floor, generator_a_floor), (microchip_b_floor, generator_b_floor), ...]
    // ///
    // /// E.g. given the initial state in the Day 11 description, we would have...
    // /// [(0, 1), (0, 2)]
    // ///
    // /// which means:
    // /// - (hydrogen microchip is on floor 0, hydrogen generator is on floor 1)
    // /// - (lithium microchip is on floor 0, lithium generator is on floor 2)
    // ///
    // /// We would end up with exactly the same shape if the hydrogen and lithium items were swapped.
    // /// This makes it easy to tell if we've previously seen an equivalent state
    // pub fn get_shape(&self) -> Vec<(usize, usize)> {
    //     let (microchips, generators): (Vec<(String, usize)>, Vec<(String, usize)>) = self.floors.iter()
    //         .enumerate()
    //         .flat_map(|(idx, f)| {
    //             f.items.clone()
    //                 .iter()
    //                 .map(|i| (i.clone(), idx))
    //                 .collect::<Vec<(Item, usize)>>()
    //         })
    //         .partition_map(|(item, index)| {
    //             match item {
    //                 Item::Microchip(name) => Either::Left((name, index)),
    //                 Item::Generator(name) => Either::Right((name, index)),
    //             }
    //         });
    //
    //     let generators: HashMap<String, usize> = generators.into_iter().collect();
    //
    //     microchips.iter()
    //         .map(|(name, index)| {
    //             let generator_index = generators.get(name).unwrap();
    //             (*index, *generator_index)
    //         })
    //         .sorted_by(|(mc_a, gen_a), (mc_b, gen_b)| {
    //             if mc_a != mc_b {
    //                 Ord::cmp(mc_a, mc_b)
    //             } else {
    //                 Ord::cmp(gen_a, gen_b)
    //             }
    //         })
    //         .collect::<Vec<(usize, usize)>>()
    // }
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
