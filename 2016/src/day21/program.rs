use crate::day21::command::Command;
use std::str::FromStr;
use itertools::Itertools;

#[derive(Debug)]
pub struct Program {
    commands: Vec<Command>
}

impl Program {
    pub fn new(commands: Vec<Command>) -> Self {
        Self { commands }
    }

    pub fn execute(&self, input: &str) -> String {
        let input: Vec<char> = input.chars().collect();
        let result = self.commands.iter().fold(input, |input, cmd| cmd.execute(input));
        result.iter().map(|c| c.to_string()).join("")
    }
}

impl FromStr for Program {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let commands: Vec<Command> = s
            .lines()
            .map(|line| line.trim().parse::<Command>().unwrap())
            .collect();

        Ok(Self::new(commands))
    }
}