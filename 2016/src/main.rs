mod utils;
mod day1;
mod day2;
mod day3;

use std::env;
use std::process::exit;
use std::path::Path;

fn main() {
    let args: Vec<String> = env::args().skip(1).collect();
    if args.len() < 2 {
        eprintln!("Expected arguments: <day> <filename>.");
        exit(1);
    }

    let day_part = args[0].as_str();
    let filename = args[1].as_str();
    let path = Path::new(filename);

    let result = match day_part {
        "1" => day1::run(path),
        "2" => day2::run(path),
        "3" => day3::run(path),
        _ => Err("Unsupported day"),
    };

    println!("Result: {}", result.expect("Error occurred."));
}
