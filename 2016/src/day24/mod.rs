use std::path::Path;
use crate::utils::Answers;
use std::fs::read_to_string;
use self::grid::Grid;

mod cell;
mod grid;

pub fn run(path: &Path) -> Result<Answers, &'static str> {
    let _content = read_to_string(path).unwrap().trim().to_string();

    let grid: Grid = _content.parse().unwrap();

    Ok(Answers {
        part1: grid.shortest_path_to_visit_all(0, None).to_string(),
        part2: grid.shortest_path_to_visit_all(0, Some(0)).to_string(),
    })
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {
        let input = "###########\n#0.1.....2#\n#.#######.#\n#4.......3#\n###########";
        let grid: Grid = input.parse().unwrap();
        assert_eq!(14, grid.shortest_path_to_visit_all(0, None));
        assert_eq!(20, grid.shortest_path_to_visit_all(0, Some(0)));
    }
}
