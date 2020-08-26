use std::path::Path;
use crate::utils::Answers;
use std::fs::read_to_string;

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let input = read_to_string(path).unwrap().trim().to_string();

    Ok(Answers {
        part1: crack_password(&input).to_string(),
        part2: crack_second_password(&input).to_string(),
    })
}

fn crack_password(input: &str) -> String {
    let mut i = 0u64;
    let mut password = String::new();
    loop {
        let hash = create_hash(input, &i);
        if hash.starts_with("00000") {
            password.push(hash.chars().skip(5).take(1).collect::<Vec<char>>()[0]);
            if password.len() == 8 {
                break;
            }
        }

        i += 1;
    }

    password
}

fn crack_second_password(input: &str) -> String {
    let mut password: [char; 8] = ['\0'; 8];
    let mut i = 0u64;
    loop {
        let hash = create_hash(input, &i);
        if hash.starts_with("00000") {
            let instruction = hash.chars().skip(5).take(2).collect::<Vec<char>>();
            let pos = instruction[0];
            if ['0', '1', '2', '3', '4', '5', '6', '7'].contains(&pos) {
                let index = pos.to_digit(10).unwrap() as usize;
                let value = instruction[1];
                if password[index] == '\0' {
                    password[index] = value;
                }
            }
        }

        i += 1;
        if !password.contains(&'\0') {
            break;
        }
    }

    password
        .iter()
        .map(|ch| ch.to_string())
        .collect::<Vec<String>>()
        .join("")
}

fn create_hash(input: &str, i: &u64) -> String {
    format!("{:x}", md5::compute(format!("{}{}", input, i)))
}