pub fn to_bit_vec(number: u32) -> Vec<bool> {
    let mut n = 0;
    while 2u32.pow(n) < number {
        n += 1;
    }

    let mut bit_vec: Vec<bool> = Vec::new();
    let mut remainder = number;
    for n in (0..n).rev() {
        let x = 2u32.pow(n);
        if x <= remainder {
            bit_vec.push(true);
            remainder -= x;
        } else {
            bit_vec.push(false);
        }
    }

    bit_vec
}

pub fn from_bit_vec(bit_vec: Vec<bool>) -> u32 {
    bit_vec.iter()
        .rev()
        .enumerate()
        .fold(0, |acc, (i, &bit)|
            acc + if bit {
                2u32.pow(i as u32)
            } else {
                0
            }
        )
}