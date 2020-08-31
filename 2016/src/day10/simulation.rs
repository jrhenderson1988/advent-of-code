use crate::day10::instruction::{Instruction, Recipient};
use std::collections::HashMap;

#[derive(Debug)]
pub struct Bot {
    id: u32,
    values: Vec<u32>,
    low: Recipient,
    high: Recipient,
}

impl Bot {
    pub fn add_value(&mut self, value: u32) {
        self.values.push(value);
    }

    pub fn can_execute(&self) -> bool {
        self.values.len() == 2
    }

    pub fn get_id(&self) -> u32 {
        self.id
    }

    pub fn execute(&mut self) -> (Recipient, u32, Recipient, u32) {
        if !self.can_execute() {
            panic!("Can not execute.")
        }

        let low_value: u32 = self.values.iter().cloned().min().unwrap();
        let high_value: u32 = self.values.iter().cloned().max().unwrap();

        let low_idx = self.values.iter().cloned().position(|value| value == low_value).unwrap();
        self.values.remove(low_idx as usize);

        let high_idx = self.values.iter().cloned().position(|value| value == high_value).unwrap();
        self.values.remove(high_idx as usize);

        (self.low.clone(), low_value, self.high.clone(), high_value)
    }
}

#[derive(Debug)]
pub struct Simulation {
    bots: HashMap<u32, Bot>,
    outputs: HashMap<u32, u32>
}

impl Simulation {
    pub fn new(instructions: &Vec<Instruction>) -> Self {
        let mut bots: HashMap<u32, Bot> = HashMap::new();

        let mut initial_values: Vec<(Recipient, u32)> = vec![];
        for instruction in instructions.iter().cloned() {
            match instruction {
                Instruction::Give { bot_id, low, high } => {
                    let bot = Bot { id: bot_id, values: vec![], low, high };
                    bots.insert(bot_id, bot);
                }
                Instruction::Receive { recipient, value } => {
                    initial_values.push((recipient, value));
                }
            }
        }

        for (recipient, value) in initial_values {
            if let Recipient::Bot(id) = recipient {
                let bot = bots.get_mut(&id).unwrap();
                bot.add_value(value);
            }
        }

        let outputs: HashMap<u32, u32> = HashMap::new();

        Simulation { bots, outputs }
    }

    pub fn find_bot_that_compares(&mut self, a: u32, b: u32) -> Option<u32> {
        loop {
            let bot = self.bots.values()
                .into_iter()
                .find(|bot| {
                    bot.values.contains(&a) && bot.values.contains(&b)
                });

            if bot.is_some() {
                return Some(bot.unwrap().get_id())
            }

            if !self.round() {
                break;
            }
        }

        None
    }

    pub fn run_to_completion(&mut self) {
        loop {
            if !self.round() {
                break;
            }
        }
    }

    pub fn multiply_values_in_outputs(&mut self, outputs: Vec<u32>) -> u32 {
        self.run_to_completion();

        self.outputs.iter()
            .filter(|(id, _value)| outputs.contains(&id))
            .fold(1, |acc, (_id, value)| acc * value)
    }

    pub fn round(&mut self) -> bool {
        let mut bot_updates: Vec<(u32, u32)> = vec![];
        let mut output_updates: Vec<(u32, u32)> = vec![];

        self.bots.iter_mut()
            .filter(|(_id, bot)| bot.can_execute())
            .for_each(|(_id, bot)| {
                let (low_recipient, low_value, high_recipient, high_value) = bot.execute();

                match low_recipient {
                    Recipient::Bot(id) => bot_updates.push((id, low_value)),
                    Recipient::Output(id) => output_updates.push((id, low_value)),
                }

                match high_recipient {
                    Recipient::Bot(id) => bot_updates.push((id, high_value)),
                    Recipient::Output(id) => output_updates.push((id, high_value))
                }

                // println!("Low: ({:?}, {}), High: ({:?}, {})", low_recipient, low_value, high_recipient, high_value);
                // println!("{:?}", bot);
            });

        bot_updates.iter()
            .cloned()
            .for_each(|(id, value)| {
                let bot = self.bots.get_mut(&id).unwrap();
                bot.add_value(value);
                // println!("Updated bot: {:?}", bot);
            });

        output_updates.iter()
            .cloned()
            .for_each(|(id, value)| {
                self.outputs.insert(id, value);
            });

        bot_updates.len() > 0 || output_updates.len() > 0
    }
}
