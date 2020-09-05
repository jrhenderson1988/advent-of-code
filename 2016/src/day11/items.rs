#[derive(Eq, PartialEq, Clone, Debug, Hash, Ord, PartialOrd)]
pub enum Item {
    Microchip(String),
    Generator(String)
}
