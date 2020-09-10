#[derive(Eq, PartialEq, Clone, Debug, Hash, Ord, PartialOrd)]
pub enum Item {
    Microchip(String),
    Generator(String),
}

impl Item {
    pub fn safe_to_move_with(&self, other: &Self) -> bool {
        match self {
            Item::Microchip(name) => {
                match other {
                    Item::Microchip(_) => true,
                    Item::Generator(other_name) => other_name == name
                }
            }
            Item::Generator(_) => {
                match other {
                    Item::Generator(_) => true,
                    Item::Microchip(other_name) => other_name == other_name
                }
            }
        }
    }
}
