use crate::utils::bit_vec::{from_bit_vec, to_bit_vec};

pub struct Circle {
    total_elves: u32
}

impl Circle {
    pub fn new(total_elves: u32) -> Self {
        Self { total_elves }
    }

    pub fn elf_with_all_the_presents(&self) -> u32 {
        let mut bv = to_bit_vec(self.total_elves);
        {
            let msb = *bv.first().unwrap();
            bv.remove(0);
            bv.push(msb);
        }

        from_bit_vec(bv)
    }
}