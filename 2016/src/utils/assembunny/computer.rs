use crate::utils::assembunny::instruction::{Instruction, RegisterId, ValueOrRegister};
use std::collections::HashMap;
use crate::utils::assembunny::instruction::ValueOrRegister::Value;
use ValueOrRegister::Register;

#[derive(Debug)]
pub struct Computer {
    registers: HashMap<RegisterId, i32>,
    instruction_pointer: usize,
}

impl Computer {
    pub fn new(registers_ids: Vec<RegisterId>) -> Self {
        let mut registers = HashMap::new();
        registers_ids.iter().for_each(|&register_id| {
            registers.insert(register_id, 0);
        });

        Self { registers, instruction_pointer: 0 }
    }

    pub fn execute(&mut self, program: &Vec<Instruction>) {
        let mut instructions: Vec<Instruction> = program.to_owned().to_vec();
        let program_length = instructions.len();
        while self.instruction_pointer < program_length {
            let instruction = instructions.get(self.instruction_pointer).unwrap();
            match instruction {
                Instruction::Copy(x, y) => {
                    // y MUST be a register, x can be a value or a register
                    let to = match y {
                        Value(_) => continue,
                        Register(r) => r
                    };

                    let value = self.get_value_for_value_or_register(x);
                    self.set_register_value(*to, value);
                }
                Instruction::Increment(x) => {
                    // x MUST be a register
                    let register = match x {
                        Value(_) => continue,
                        Register(r) => r,
                    };

                    let value = self.get_register_value(*register).unwrap();
                    self.set_register_value(*register, value + 1);
                }
                Instruction::Decrement(x) => {
                    // x MUST be a register
                    let register = match x {
                        Value(_) => continue,
                        Register(r) => r,
                    };

                    let value = self.get_register_value(*register).unwrap();
                    self.set_register_value(*register, value - 1);
                }
                Instruction::Jump(x, y) => {
                    // either x or y can be either register or value
                    let value = self.get_value_for_value_or_register(x);
                    let amount = self.get_value_for_value_or_register(y);
                    if value != 0 {
                        // -1 to offset the incoming + 1
                        let new_ip = (self.instruction_pointer as i32) + amount - 1;
                        if new_ip < 0 {
                            panic!("Invalid jump. Instruction pointer would become negative.");
                        }

                        self.instruction_pointer = new_ip as usize;
                    }
                }
                Instruction::Toggle(x) => {
                    let value = self.get_value_for_value_or_register(x);
                    let idx = self.instruction_pointer + (value as usize);
                    if let Some(ins) = instructions.get(idx) {
                        let new_instruction = match ins {
                            Instruction::Increment(x) => Instruction::Decrement(x.clone()),
                            Instruction::Decrement(x) => Instruction::Increment(x.clone()),
                            Instruction::Toggle(x) => Instruction::Increment(x.clone()),
                            Instruction::Jump(x, y) => Instruction::Copy(x.clone(), y.clone()),
                            Instruction::Copy(x, y) => Instruction::Jump(x.clone(), y.clone()),
                        };

                        if let Some(instruction) = instructions.get_mut(idx) {
                            *instruction = new_instruction;
                        }
                    }
                }
            }

            self.instruction_pointer += 1;
        }
    }

    pub fn get_register_value(&self, register: RegisterId) -> Option<i32> {
        let result = self.registers.get(&register);
        match result {
            Some(value) => Some(*value),
            None => None
        }
    }

    pub fn set_register_value(&mut self, register: RegisterId, value: i32) {
        if !self.registers.contains_key(&register) {
            panic!(format!("Invalid register '{}'", register))
        }

        self.registers.insert(register, value);
    }

    fn get_value_for_value_or_register(&self, value_or_register: &ValueOrRegister) -> i32 {
        match value_or_register {
            ValueOrRegister::Value(v) => *v,
            Register(id) => {
                self.get_register_value(*id).unwrap()
            }
        }
    }
}

