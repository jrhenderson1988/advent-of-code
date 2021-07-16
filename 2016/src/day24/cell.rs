#[derive(Debug)]
pub enum Cell {
    Wall,
    Space,
    Number(u8),
}

impl Cell {
    pub fn is_a_number(&self) -> bool {
        match self {
            Cell::Number(_) => true,
            _ => false,
        }
    }

    // pub fn is_number(&self, number: &u8) -> bool {
    //     match self {
    //         Cell::Number(n) => n == number,
    //         _ => false,
    //     }
    // }

    pub fn get_number(&self) -> u8 {
        match self {
            Cell::Number(n) => *n,
            _ => panic!("not a number")
        }
    }
}