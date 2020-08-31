use std::str::FromStr;
use regex::Regex;

#[derive(Debug, Clone)]
pub enum Recipient {
    Output(u32),
    Bot(u32),
}

impl Recipient {
    pub fn make(recipient_type: &str, id: u32) -> Self {
        match recipient_type {
            "bot" => Self::Bot(id),
            "output" => Self::Output(id),
            _ => panic!("Invalid recipient type")
        }
    }
}

#[derive(Debug, Clone)]
pub enum Instruction {
    Receive { recipient: Recipient, value: u32 },
    Give { bot_id: u32, low: Recipient, high: Recipient },
}

impl FromStr for Instruction {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        if s.starts_with("value") {
            let pattern = Regex::new(r"^value (\d+) goes to (bot|output) (\d+)$").unwrap();
            let captures = pattern.captures(s).unwrap();
            let value = captures.get(1).unwrap().as_str().parse::<u32>().unwrap();
            let recipient_type = captures.get(2).unwrap().as_str();
            let recipient_id = captures.get(3).unwrap().as_str().parse::<u32>().unwrap();
            let recipient = Recipient::make(recipient_type, recipient_id);
            Ok(Instruction::Receive { recipient, value })
        } else if s.starts_with("bot") {
            let pattern = Regex::new(
                r"^bot (\d+) gives low to (bot|output) (\d+) and high to (bot|output) (\d+)$"
            ).unwrap();
            let captures = pattern.captures(s).unwrap();
            let bot = captures.get(1).unwrap().as_str().parse::<u32>().unwrap();
            let low_recipient_type = captures.get(2).unwrap().as_str();
            let low_recipient_id = captures.get(3).unwrap().as_str().parse::<u32>().unwrap();
            let high_recipient_type = captures.get(4).unwrap().as_str();
            let high_recipient_id = captures.get(5).unwrap().as_str().parse::<u32>().unwrap();
            let low = Recipient::make(low_recipient_type, low_recipient_id);
            let high = Recipient::make(high_recipient_type, high_recipient_id);
            Ok(Instruction::Give { bot_id: bot, low, high })
        } else {
            Err("Invalid instruction.".to_string())
        }
    }
}