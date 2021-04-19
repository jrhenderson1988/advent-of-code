use std::path::Path;
use crate::utils::Answers;
use std::fs::read_to_string;
use crate::utils::assembunny::instruction::Instruction;
use crate::utils::assembunny::computer::Computer;

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let content = read_to_string(path).unwrap().trim().to_string();
    let instructions = content.lines()
        .map(|line| line.parse::<Instruction>().unwrap())
        .collect::<Vec<Instruction>>();
    let mut cpu = Computer::new(vec!['a', 'b', 'c', 'd']);
    cpu.set_register_value('a', 7);
    cpu.execute(&instructions);
    let part1 = cpu.get_register_value('a');

    // No need for manual analysis and optimising the loop by hand when Rust is
    // able to work out the answer in a few of minutes!
    let mut cpu = Computer::new(vec!['a', 'b', 'c', 'd']);
    cpu.set_register_value('a', 12);
    cpu.execute(&instructions);
    let part2 = cpu.get_register_value('a');

    Ok(Answers {
        part1: part1.unwrap().to_string(),
        part2: part2.unwrap().to_string(),
    })
}

#[cfg(test)]
mod tests {
    use crate::utils::assembunny::instruction::Instruction;
    use crate::utils::assembunny::computer::Computer;

    #[test]
    fn test() {
        let content = "cpy 2 a\ntgl a\ntgl a\ntgl a\ncpy 1 a\ndec a\ndec a";
        let instructions = content.lines()
            .map(|line| line.parse::<Instruction>().unwrap())
            .collect::<Vec<Instruction>>();
        let mut cpu = Computer::new(vec!['a', 'b', 'c', 'd']);
        cpu.execute(&instructions);
        let result = cpu.get_register_value('a');
        assert!(result.is_some());
        assert_eq!(3, result.unwrap());
    }
}


