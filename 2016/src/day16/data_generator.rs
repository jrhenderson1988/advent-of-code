pub struct DataGenerator {
    initial: String
}

impl DataGenerator {
    pub fn new(initial: String) -> Self {
        Self { initial }
    }

    pub fn generate_for_length(&self, length: usize) -> String {
        let mut data = self.initial.clone();
        while data.len() < length {
            data = format!(
                "{}0{}",
                data,
                data.chars()
                    .into_iter()
                    .rev()
                    .map(|c|
                        match c {
                            '1' => '0',
                            '0' => '1',
                            _ => c
                        }
                    )
                    .collect::<String>()
            );
        }

        data.chars().take(length).collect()
    }

    pub fn checksum_for_length(&self, length: usize) -> String {
        self.checksum_for_data(self.generate_for_length(length))
    }

    pub fn checksum_for_data(&self, data: String) -> String {
        let mut checksum = data.clone();
        while checksum.len() % 2 == 0 {
            checksum = checksum.chars()
                .collect::<Vec<char>>()
                .chunks(2)
                .map(|chars|
                    if chars[0] == chars[1] {
                        '1'
                    } else {
                        '0'
                    }
                )
                .collect::<String>()
        }

        checksum
    }
}