use std::str::FromStr;

#[derive(Debug)]
pub struct CompressedFile {
    characters: Vec<char>
}

#[derive(Debug)]
pub struct Instruction {
    start: usize,
    end: usize,
    length: usize,
    repeat: u32,
}

#[derive(Debug)]
pub enum InstructionOrChar {
    Instruction(Instruction),
    Char(char),
}

impl CompressedFile {
    pub fn new(characters: Vec<char>) -> Self {
        CompressedFile { characters }
    }

    pub fn calculate_decompressed_length(&self) -> u64 {
        self.segment_length(&self.characters, false)
    }

    pub fn calculate_decompressed_v2_length(&self) -> u64 {
        self.segment_length(&self.characters, true)
    }

    fn segment_length(&self, segment: &Vec<char>, v2: bool) -> u64 {
        let mut i = 0;
        let segment_len = segment.len();
        let mut total = 0u64;

        loop {
            if i >= segment_len {
                break;
            }

            match self.parse(i, segment) {
                InstructionOrChar::Char(_) => {
                    total += 1;
                    i += 1;
                }
                InstructionOrChar::Instruction(instruction) => {
                    let repeat = instruction.repeat as u64;
                    if v2 {
                        let child_segment = segment.iter()
                            .skip(instruction.end + 1)
                            .take(instruction.length)
                            .cloned()
                            .collect::<Vec<char>>();

                        total += self.segment_length(&child_segment, v2) * repeat;
                    } else {
                        total += (instruction.length as u64) * repeat;
                    }

                    i = instruction.end + 1 + instruction.length;
                }
            }
        }

        total
    }

    fn parse(&self, i: usize, characters: &Vec<char>) -> InstructionOrChar {
        if characters[i] != '(' {
            InstructionOrChar::Char(characters[i])
        } else {
            let start = i;
            let mut i = i + 1;
            let mut x_count = 0;
            loop {
                let ch: char = characters[i];
                if ch == ')' && x_count == 1 {
                    let end = i;
                    let content = characters.iter()
                        .skip(start)
                        .take(end - start + 1)
                        .cloned()
                        .collect::<Vec<char>>();

                    let x_pos = content.iter().position(|c| *c == 'x').unwrap();
                    let length = content.iter()
                        .skip(1)
                        .take(x_pos - 1)
                        .cloned()
                        .map(|c| c.to_string())
                        .collect::<Vec<String>>()
                        .join("")
                        .parse::<usize>()
                        .unwrap();
                    let repeat = content.iter()
                        .skip(x_pos + 1)
                        .take(content.len() - x_pos - 2)
                        .cloned()
                        .map(|c| c.to_string())
                        .collect::<Vec<String>>()
                        .join("")
                        .parse::<u32>()
                        .unwrap();

                    return InstructionOrChar::Instruction(
                        Instruction { start, end, length, repeat }
                    );
                } else if ch == 'x' {
                    x_count += 1;
                } else if (!ch.is_digit(10) && ch != 'x')
                    || (x_count > 0 && ch == 'x')
                    || (ch == ')' && x_count < 1) {
                    break;
                }

                i += 1;
            }

            InstructionOrChar::Char(characters[start])
        }
    }
}

impl FromStr for CompressedFile {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let characters = s.chars().collect::<Vec<char>>();
        Ok(CompressedFile::new(characters))
    }
}