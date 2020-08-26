mod utils;
mod day1;
mod day2;
mod day3;
mod day4;
mod day5;
mod day6;

use std::env;
use std::process::exit;
use std::path::Path;
use std::time::SystemTime;

fn main() {
    let args: Vec<String> = env::args().skip(1).collect();
    if args.len() < 2 {
        eprintln!("Expected arguments: <day> <filename>.");
        exit(1);
    }

    let day_part = args[0].as_str();
    let filename = args[1].as_str();
    let path = Path::new(filename);

    let start = SystemTime::now();
    let result = match day_part {
        "1" => day1::run(path),
        "2" => day2::run(path),
        "3" => day3::run(path),
        "4" => day4::run(path),
        "5" => day5::run(path),
        "6" => day6::run(path),
        _ => Err("Unsupported day"),
    };

    match start.elapsed() {
        Ok(elapsed) => println!("Results in {}ms", elapsed.as_millis()),
        Err(e) => eprintln!("Error: {:?}", e)
    }

    println!("{}", result.expect("Error occurred."));
}
