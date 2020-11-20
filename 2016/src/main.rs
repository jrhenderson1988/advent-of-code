mod utils;
mod day01;
mod day02;
mod day03;
mod day04;
mod day05;
mod day06;
mod day07;
mod day08;
mod day09;
mod day10;
mod day11;
mod day12;
mod day13;
mod day14;
mod day15;
mod day16;
mod day17;
mod day18;
mod day19;
mod day20;
mod day21;

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
        "1" | "01" => day01::run(path),
        "2" | "02" => day02::run(path),
        "3" | "03" => day03::run(path),
        "4" | "04" => day04::run(path),
        "5" | "05" => day05::run(path),
        "6" | "06" => day06::run(path),
        "7" | "07" => day07::run(path),
        "8" | "08" => day08::run(path),
        "9" | "09" => day09::run(path),
        "10" => day10::run(path),
        "11" => day11::run(path),
        "12" => day12::run(path),
        "13" => day13::run(path),
        "14" => day14::run(path),
        "15" => day15::run(path),
        "16" => day16::run(path),
        "17" => day17::run(path),
        "18" => day18::run(path),
        "19" => day19::run(path),
        "20" => day20::run(path),
        "21" => day21::run(path),
        _ => Err("Unsupported day"),
    };

    match start.elapsed() {
        Ok(elapsed) => println!("Results in {}ms", elapsed.as_millis()),
        Err(e) => eprintln!("Error: {:?}", e)
    }

    println!("{}", result.expect("Error occurred."));
}
