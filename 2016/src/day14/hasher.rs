use md5;
use std::collections::HashMap;

pub struct Hasher {
    salt: String,
    stretch: u32,
    cache: HashMap<u64, String>
}

impl Hasher {
    pub fn new(salt: String, stretch: u32) -> Self {
        Self { salt, stretch, cache: HashMap::new() }
    }

    pub fn hash(&mut self, i: &u64) -> String {
        if let Some(hash) = self.cache.get(i) {
            hash.clone()
        } else {
            let hash = (0..self.stretch)
                .fold(
                    self.md5(format!("{}{}", self.salt, i)),
                    |hash, _| self.md5(hash)
                );
            self.cache.insert(*i, hash.clone());
            hash.clone()
        }
    }

    fn md5(&self, input: String) -> String {
        format!("{:x}", md5::compute(input))
    }
}