use std::fs::read_to_string;
use std::path::Path;

use crate::utils::Answers;
use crate::utils::assembunny::computer::Computer;
use crate::utils::assembunny::instruction::Instruction;

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let content = read_to_string(path).unwrap().trim().to_string();
    let instructions = content.lines()
        .map(|line| line.parse::<Instruction>().unwrap())
        .collect::<Vec<Instruction>>();

    let targets: Vec<Vec<i32>> = vec![
        vec![1, 0, 1, 0, 1, 0, 1, 0, 1, 0],
        vec![0, 1, 0, 1, 0, 1, 0, 1, 0, 1]
    ];

    let mut i = 0;
    loop {
        let mut cpu = Computer::new(vec!['a', 'b', 'c', 'd']);
        cpu.set_register_value('a', i);
        if cpu.execute_until(&instructions, &targets) {
            break;
        }
        i += 1;
    }

    Ok(Answers {
        part1: i.to_string(),
        part2: "N/A".to_string(),
    })
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {}
}
