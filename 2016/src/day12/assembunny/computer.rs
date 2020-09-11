use crate::day12::assembunny::instruction::{Instruction, RegisterId, ValueOrRegister};
use std::collections::HashMap;

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
        let program_length = program.len();
        while self.instruction_pointer < program_length {
            let instruction = program.get(self.instruction_pointer).unwrap();
            match instruction {
                Instruction::Copy(from, to) => {
                    let value = self.get_value_for_value_or_register(from);
                    self.set_register_value(*to, value);
                }
                Instruction::Increment(register) => {
                    let value = self.get_register_value(*register).unwrap();
                    self.set_register_value(*register, value + 1);
                }
                Instruction::Decrement(register) => {
                    let value = self.get_register_value(*register).unwrap();
                    self.set_register_value(*register, value - 1);
                }
                Instruction::Jump(value_or_register, amount) => {
                    let value = self.get_value_for_value_or_register(value_or_register);
                    if value != 0 {
                        // -1 to offset the incoming + 1
                        let new_ip = (self.instruction_pointer as i32) + amount - 1;
                        if new_ip < 0 {
                            panic!("Invalid jump. Instruction pointer would become negative.");
                        }

                        self.instruction_pointer = new_ip as usize;
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
            ValueOrRegister::Register(id) => {
                self.get_register_value(*id).unwrap()
            }
        }
    }
}