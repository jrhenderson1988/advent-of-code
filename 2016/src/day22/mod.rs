mod node;
mod cluster;

use std::path::Path;
use crate::utils::Answers;
use std::fs::read_to_string;
use crate::day22::cluster::Cluster;

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let input = read_to_string(path).unwrap().trim().to_string();
    let cluster: Cluster = input.parse().unwrap();

    Ok(Answers {
        part1: cluster.total_viable_pairs().to_string(),
        part2: cluster.minimum_steps_to_move_target_data_to_start().to_string(),
    })
}



