use std::str::FromStr;
use regex::Regex;
use std::cmp::{min, max};

#[derive(Debug)]
pub enum Command {
    SwapPosition { x: usize, y: usize },
    SwapLetter { x: char, y: char },
    RotateSteps { right: bool, steps: usize },
    RotateFromLetter { letter: char },
    ReversePositions { x: usize, y: usize },
    Move { x: usize, y: usize },
}

impl Command {
    pub fn execute(&self, letters: Vec<char>) -> Vec<char> {
        let letters_len = letters.len();
        match self {
            Command::SwapPosition { x, y } => {
                let mut letters = letters.to_vec();
                letters.swap(*x, *y);
                letters
            }
            Command::SwapLetter { x, y } => {
                let x_pos = letters.iter().position(|c| c == x).unwrap();
                let y_pos = letters.iter().position(|c| c == y).unwrap();
                let mut letters = letters.to_vec();
                letters.swap(x_pos, y_pos);
                letters
            }
            Command::RotateSteps { right, steps } => {
                let mut letters = letters.to_vec();
                if *right {
                    letters.rotate_right(*steps % letters_len)
                } else {
                    letters.rotate_left(*steps % letters_len)
                }
                letters
            }
            Command::RotateFromLetter { letter } => {
                let mut letters = letters.to_vec();
                let idx = letters.iter().position(|l| l == letter).unwrap();
                letters.rotate_right((1 + idx + if idx >= 4 { 1 } else { 0 }) % letters_len);

                letters
            }
            Command::ReversePositions { x, y } => {
                let min = min(x, y);
                let max = max(x, y);
                let mut new_letters: Vec<char> = letters.to_vec().into_iter().take(*min).collect();
                new_letters.extend(letters.to_vec().into_iter().skip(*min).take(*max + 1 - *min).rev());
                new_letters.extend(letters.to_vec().into_iter().skip(*max + 1));

                new_letters
            }
            Command::Move { x, y } => {
                let mut new_letters = letters.to_vec();
                let c: char = new_letters.remove(*x);
                new_letters.insert(*y, c);
                new_letters
            }
        }
    }
}

impl FromStr for Command {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let swap_position = Regex::new(r"swap position (\d+) with position (\d+)").unwrap();
        let swap_letter = Regex::new(r"swap letter ([a-z]+) with letter ([a-z]+)").unwrap();
        let rotate_steps = Regex::new(r"rotate (right|left) (\d+) steps?").unwrap();
        let rotate_from_letter = Regex::new(r"rotate based on position of letter ([a-z]+)").unwrap();
        let reverse_positions = Regex::new(r"reverse positions (\d+) through (\d+)").unwrap();
        let move_position = Regex::new(r"move position (\d+) to position (\d+)").unwrap();

        if swap_position.is_match(s) {
            let captures = swap_position.captures(s).unwrap();
            let x: usize = captures[1].parse().unwrap();
            let y: usize = captures[2].parse().unwrap();
            Ok(Command::SwapPosition { x, y })
        } else if swap_letter.is_match(s) {
            let captures = swap_letter.captures(s).unwrap();
            let x: char = captures[1].parse().unwrap();
            let y: char = captures[2].parse().unwrap();
            Ok(Command::SwapLetter { x, y })
        } else if rotate_steps.is_match(s) {
            let captures = rotate_steps.captures(s).unwrap();
            let direction: &str = &captures[1];
            let steps: usize = captures[2].parse().unwrap();
            Ok(Command::RotateSteps { right: direction == "right", steps })
        } else if rotate_from_letter.is_match(s) {
            let captures = rotate_from_letter.captures(s).unwrap();
            let letter: char = captures[1].parse().unwrap();
            Ok(Command::RotateFromLetter { letter })
        } else if reverse_positions.is_match(s) {
            let captures = reverse_positions.captures(s).unwrap();
            let x: usize = captures[1].parse().unwrap();
            let y: usize = captures[2].parse().unwrap();
            Ok(Command::ReversePositions { x, y })
        } else if move_position.is_match(s) {
            let captures = move_position.captures(s).unwrap();
            let x: usize = captures[1].parse().unwrap();
            let y: usize = captures[2].parse().unwrap();
            Ok(Command::Move { x, y })
        } else {
            Err("unexpected command".to_string())
        }
    }
}

#[cfg(test)]
mod tests {
    use crate::day21::command::Command;

    #[test]
    fn swap_positions() {
        let result = Command::SwapPosition { x: 0, y: 1 }.execute(vec!['a', 'b', 'c']);
        assert_eq!(vec!['b', 'a', 'c'], result);
    }

    #[test]
    fn swap_letters() {
        let result = Command::SwapLetter { x: 'a', y: 'c' }.execute(vec!['a', 'b', 'c']);
        assert_eq!(vec!['c', 'b', 'a'], result);
    }

    #[test]
    fn rotate_right() {
        assert_eq!(
            vec!['a', 'b', 'c'],
            Command::RotateSteps { right: true, steps: 0 }.execute(vec!['a', 'b', 'c'])
        );
        assert_eq!(
            vec!['c', 'a', 'b'],
            Command::RotateSteps { right: true, steps: 1 }.execute(vec!['a', 'b', 'c'])
        );
        assert_eq!(
            vec!['b', 'c', 'a'],
            Command::RotateSteps { right: true, steps: 2 }.execute(vec!['a', 'b', 'c'])
        );
        assert_eq!(
            vec!['a', 'b', 'c'],
            Command::RotateSteps { right: true, steps: 3 }.execute(vec!['a', 'b', 'c'])
        );
        assert_eq!(
            Command::RotateSteps { right: true, steps: 1 }.execute(vec!['a', 'b', 'c']),
            Command::RotateSteps { right: true, steps: 7 }.execute(vec!['a', 'b', 'c'])
        );
    }

    #[test]
    fn rotate_left() {
        assert_eq!(
            vec!['a', 'b', 'c'],
            Command::RotateSteps { right: false, steps: 0 }.execute(vec!['a', 'b', 'c'])
        );
        assert_eq!(
            vec!['b', 'c', 'a'],
            Command::RotateSteps { right: false, steps: 1 }.execute(vec!['a', 'b', 'c'])
        );
        assert_eq!(
            vec!['c', 'a', 'b'],
            Command::RotateSteps { right: false, steps: 2 }.execute(vec!['a', 'b', 'c'])
        );
        assert_eq!(
            vec!['a', 'b', 'c'],
            Command::RotateSteps { right: false, steps: 3 }.execute(vec!['a', 'b', 'c'])
        );
        assert_eq!(
            Command::RotateSteps { right: false, steps: 1 }.execute(vec!['a', 'b', 'c']),
            Command::RotateSteps { right: false, steps: 7 }.execute(vec!['a', 'b', 'c'])
        );
    }

    #[test]
    fn rotate_from_letter() {
        assert_eq!(
            vec!['e', 'c', 'a', 'b', 'd'],
            Command::RotateFromLetter { letter: 'b' }.execute(vec!['a', 'b', 'd', 'e', 'c'])
        );

        assert_eq!(
            vec!['d', 'e', 'c', 'a', 'b'],
            Command::RotateFromLetter { letter: 'd' }.execute(vec!['e', 'c', 'a', 'b', 'd'])
        )
    }

    #[test]
    fn reverse_positions() {
        assert_eq!(
            vec!['a', 'b', 'c', 'd', 'e'],
            Command::ReversePositions { x: 0, y: 4 }.execute(vec!['e', 'd', 'c', 'b', 'a'])
        );
        assert_eq!(
            vec!['a', 'd', 'c', 'b', 'e'],
            Command::ReversePositions { x: 1, y: 3 }.execute(vec!['a', 'b', 'c', 'd', 'e'])
        );
    }

    #[test]
    fn move_positions() {
        assert_eq!(
            vec!['b', 'd', 'e', 'a', 'c'],
            Command::Move { x: 1, y: 4 }.execute(vec!['b', 'c', 'd', 'e', 'a'])
        );
        assert_eq!(
            vec!['a', 'b', 'd', 'e', 'c'],
            Command::Move { x: 3, y: 0 }.execute(vec!['b', 'd', 'e', 'a', 'c'])
        );
    }
}