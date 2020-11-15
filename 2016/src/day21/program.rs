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

    pub fn execute_reverse(&self, input: &str) -> String {
        let input: Vec<char> = input.chars().collect();
        let result = self.commands.iter().rev().fold(input, |input, cmd| cmd.reverse(input));
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

#[cfg(test)]
mod tests {
    use crate::day21::program::Program;
    use crate::day21::command::Command::{SwapPosition, SwapLetter, ReversePositions, RotateSteps, Move, RotateFromLetter};

    #[test]
    fn parse() {
        let instructions = "swap position 4 with position 0\n\
                            swap letter d with letter b\n\
                            reverse positions 0 through 4\n\
                            rotate left 1 step\n\
                            move position 1 to position 4\n\
                            move position 3 to position 0\n\
                            rotate based on position of letter b\n\
                            rotate based on position of letter d";
        let program: Program = instructions.parse().unwrap();
        assert_eq!(vec![
            SwapPosition { x: 4, y: 0 },
            SwapLetter { x: 'd', y: 'b' },
            ReversePositions { x: 0, y: 4 },
            RotateSteps { right: false, steps: 1 },
            Move { x: 1, y: 4 },
            Move { x: 3, y: 0 },
            RotateFromLetter { letter: 'b' },
            RotateFromLetter { letter: 'd' }
        ], program.commands);
    }

    #[test]
    fn execute() {
        let commands = vec![
            SwapPosition { x: 4, y: 0 },
            SwapLetter { x: 'd', y: 'b' },
            ReversePositions { x: 0, y: 4 },
            RotateSteps { right: false, steps: 1 },
            Move { x: 1, y: 4 },
            Move { x: 3, y: 0 },
            RotateFromLetter { letter: 'b' },
            RotateFromLetter { letter: 'd' }
        ];
        let program = Program::new(commands);
        assert_eq!("decab".to_string(), program.execute("abcde"));
    }

    #[test]
    fn reverse() {
        let commands = vec![
            SwapPosition { x: 4, y: 0 },
            SwapLetter { x: 'd', y: 'b' },
            ReversePositions { x: 0, y: 4 },
            RotateSteps { right: false, steps: 1 },
            Move { x: 1, y: 4 },
            Move { x: 3, y: 0 },
            RotateFromLetter { letter: 'b' },
            RotateFromLetter { letter: 'd' }
        ];
        let program = Program::new(commands);
        assert_eq!("abcde".to_string(), program.execute_reverse("decab"));
    }
}