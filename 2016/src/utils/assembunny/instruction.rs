use std::fmt::{Display, Formatter};
use std::str::FromStr;

pub type RegisterId = char;
pub type Value = i32;

#[derive(Debug, Clone)]
pub enum Instruction {
    Copy(ValueOrRegister, ValueOrRegister),
    Increment(ValueOrRegister),
    Decrement(ValueOrRegister),
    Jump(ValueOrRegister, ValueOrRegister),
    Toggle(ValueOrRegister),
    Transmit(ValueOrRegister)
}

impl Display for Instruction {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        write!(f, "{}", match self {
            Instruction::Copy(x, y) => format!("cpy {} {}", x, y),
            Instruction::Increment(x) => format!("inc {}", x),
            Instruction::Decrement(x) => format!("dec {}", x),
            Instruction::Jump(x, y) => format!("jnz {} {}", x, y),
            Instruction::Toggle(x) => format!("tgl {}", x),
            Instruction::Transmit(x) => format!("out {}", x),
        })
    }
}

#[derive(Debug, Clone)]
pub enum ValueOrRegister {
    Value(Value),
    Register(RegisterId),
}

impl Display for ValueOrRegister {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        write!(f, "{}", match self {
            ValueOrRegister::Value(value) => format!("{}", value),
            ValueOrRegister::Register(register) => format!("{}", register),
        })
    }
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
                let target: ValueOrRegister = parts.get(1).unwrap().parse().unwrap();
                let register: ValueOrRegister = parts.get(2).unwrap().parse().unwrap();
                Ok(Instruction::Copy(target, register))
            }
            "inc" => {
                if parts.len() != 2 {
                    panic!("Invalid 'inc' instruction")
                }
                let register: ValueOrRegister = parts.get(1).unwrap().parse().unwrap();
                Ok(Instruction::Increment(register))
            }
            "dec" => {
                if parts.len() != 2 {
                    panic!("Invalid 'dec' instruction")
                }
                let register: ValueOrRegister = parts.get(1).unwrap().parse().unwrap();
                Ok(Instruction::Decrement(register))
            }
            "jnz" => {
                if parts.len() != 3 {
                    panic!("Invalid 'jnz' instruction")
                }
                let register: ValueOrRegister = parts.get(1).unwrap().parse().unwrap();
                let jump: ValueOrRegister = parts.get(2).unwrap().parse().unwrap();
                Ok(Instruction::Jump(register, jump))
            }
            "tgl" => {
                if parts.len() != 2 {
                    panic!("Invalid 'tgl' instruction");
                }
                let register: ValueOrRegister = parts.get(1).unwrap().parse().unwrap();
                Ok(Instruction::Toggle(register))
            }
            "out" => {
                if parts.len() != 2 {
                    panic!("Invalid 'out' instruction")
                }
                let register: ValueOrRegister = parts.get(1).unwrap().parse().unwrap();
                Ok(Instruction::Transmit(register))
            }
            _ => panic!("Unknown instruction")
        }
    }
}