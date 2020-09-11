use std::str::FromStr;

pub type RegisterId = char;
pub type Value = i32;

#[derive(Debug)]
pub enum Instruction {
    Copy(ValueOrRegister, RegisterId),
    Increment(RegisterId),
    Decrement(RegisterId),
    Jump(ValueOrRegister, Value),
}

#[derive(Debug)]
pub enum ValueOrRegister {
    Value(Value),
    Register(RegisterId),
}

impl FromStr for ValueOrRegister {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let value = s.parse::<Value>();
        match value {
            Ok(number) => Ok(ValueOrRegister::Value(number)),
            Err(_) => Ok(ValueOrRegister::Register(s.chars().next().unwrap()))
        }
    }
}

impl FromStr for Instruction {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let parts = s.split(" ").collect::<Vec<&str>>();
        let code = parts.get(0).unwrap();
        match *code {
            "cpy" => {
                if parts.len() != 3 {
                    panic!("Invalid 'cpy' instruction")
                }
                let target = parts.get(1).unwrap().parse::<ValueOrRegister>().unwrap();
                let register = parts.get(2).unwrap().parse::<RegisterId>().unwrap();
                Ok(Instruction::Copy(target, register))
            }
            "inc" => {
                if parts.len() != 2 {
                    panic!("Invalid 'inc' instruction")
                }
                let register = parts.get(1).unwrap().parse::<RegisterId>().unwrap();
                Ok(Instruction::Increment(register))
            }
            "dec" => {
                if parts.len() != 2 {
                    panic!("Invalid 'dec' instruction")
                }
                let register = parts.get(1).unwrap().parse::<RegisterId>().unwrap();
                Ok(Instruction::Decrement(register))
            }
            "jnz" => {
                if parts.len() != 3 {
                    panic!("Invalid 'jnz' instruction")
                }
                let register = parts.get(1).unwrap().parse::<ValueOrRegister>().unwrap();
                let jump = parts.get(2).unwrap().parse::<Value>().unwrap();
                Ok(Instruction::Jump(register, jump))
            }
            _ => panic!("Unknown instruction")
        }
    }
}