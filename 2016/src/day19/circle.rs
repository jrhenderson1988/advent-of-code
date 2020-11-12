use crate::utils::bit_vec::{from_bit_vec, to_bit_vec};
use std::collections::VecDeque;

pub struct Circle {
    n: u32
}

impl Circle {
    pub fn new(n: u32) -> Self {
        Self { n }
    }

    pub fn elf_with_all_the_presents_p1(&self) -> u32 {
        let mut bv = to_bit_vec(self.n);
        {
            let msb = *bv.first().unwrap();
            bv.remove(0);
            bv.push(msb);
        }

        from_bit_vec(bv)
    }

    /// Approach is to split up set evenly (preferring the first to be 1 item longer when we have an
    /// odd length) into two VecDeque objects (v1 and v2). This guarantees O(1) time pushing/popping
    /// from the front/back. By splitting the two in half, we can guarantee that the target item to
    /// be removed is either at the end of v1 (if v1 is longer since we prefer the item to the left
    /// in the case of a tie) or the beginning of v2. After popping the target item from the end of
    /// v1 or the start of v2 we can then shift everything back a step so that our next iteration
    /// focuses on the second item and so on. Luckily, with a VecDeque we can pop off the front of
    /// each set and push onto the back of each set in constant time, which makes this super quick.
    ///
    /// e.g. Given that we start with a number of 5 elves:
    ///
    /// v1: [1, 2, 3], v2: [4, 5] -> v1: [1, 2], v2: [4, 5] > remove last item of v1
    /// v1: [2, 4], v2: [5, 1] -> v1: [2, 4],  v2: [1]      > shift left, remove first item of v2
    /// v1: [4, 1], v2: [2] -> v1: [4], v2: [2]             > shift left, remove last item of v1
    /// v1: [2], v2: [4] -> v1: [2], v2: []                 > shift left, remove first item of v2
    /// v1: [2], v2: []                                     > v2 empty, one item left in v1
    ///
    pub fn elf_with_all_the_presents_p2(&self) -> u32 {
        let mut v1: VecDeque<u32> = (1..=(self.n + 1) / 2).collect();
        let mut v2: VecDeque<u32> = ((self.n + 1) / 2 + 1..=self.n).collect();
        while !v2.is_empty() {
            if v1.len() > v2.len() {
                v1.pop_back();
            } else {
                v2.pop_front();
            }

            v2.push_back(v1.pop_front().unwrap());
            v1.push_back(v2.pop_front().unwrap());
        }

        *v1.front().unwrap()
    }
}