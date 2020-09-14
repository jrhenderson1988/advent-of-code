use crate::day14::hasher::Hasher;

pub struct KeyFinder {
    salt: String
}

impl KeyFinder {
    pub fn new(salt: String) -> Self {
        Self { salt }
    }

    pub fn find_index_of_nth_hash(&self, n: u64, key_stretch: u32) -> u64 {
        let mut hasher = Hasher::new(self.salt.clone(), key_stretch);

        let mut i = 0u64;
        let mut current_n = 0u64;
        loop {
            let hash = hasher.hash(&i);
            if let Some(triplet_char) = self.find_triplet(&hash) {
                for j in i + 1..i + 1001 {
                    if self.contains_quintuple(hasher.hash(&j), triplet_char) {
                        if current_n == n {
                            return i;
                        } else {
                            current_n += 1;
                        }
                    }
                }
            }

            i += 1;
        }
    }

    pub fn find_triplet(&self, hash: &String) -> Option<char> {
        let chars = hash.chars().collect::<Vec<char>>();
        for i in 0..chars.len() - 2 {
            let ch = *chars.get(i).unwrap();
            if ch == *chars.get(i + 1).unwrap() && ch == *chars.get(i + 2).unwrap() {
                return Some(ch);
            }
        }

        None
    }

    pub fn contains_quintuple(&self, hash: String, ch: char) -> bool {
        hash.contains(
            (0..5).map(|_| ch.to_string())
                .collect::<Vec<String>>()
                .join("")
                .as_str()
        )
    }
}