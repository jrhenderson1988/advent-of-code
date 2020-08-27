use std::str::FromStr;

#[derive(Debug)]
pub enum IPv7Segment {
    Supernet(Vec<char>),
    Hypernet(Vec<char>),
}

#[derive(Debug)]
pub struct IPv7 {
    sequences: Vec<IPv7Segment>
}

impl IPv7 {
    pub fn new(sequences: Vec<IPv7Segment>) -> IPv7 {
        IPv7 { sequences }
    }

    pub fn supports_tls(&self) -> bool {
        let mut sequences_contain_abba = false;
        for sequence in self.sequences.iter() {
            match sequence {
                IPv7Segment::Supernet(value) => {
                    if !sequences_contain_abba {
                        sequences_contain_abba = IPv7::contains_abba(&value);
                    }
                }
                IPv7Segment::Hypernet(value) => {
                    if IPv7::contains_abba(&value) {
                        return false;
                    }
                }
            }
        }

        sequences_contain_abba
    }

    pub fn supports_ssl(&self) -> bool {
        let abas: Vec<(char, char, char)> = self.sequences
            .iter()
            .map(|sequence| self.extract_abas(sequence))
            .fold(vec![], |acc, aba| acc.iter().cloned().chain(aba.iter().cloned()).collect());

        let supports_ssl: Option<&IPv7Segment> = self.sequences
            .iter()
            .find(|sequence| {
                match sequence {
                    IPv7Segment::Hypernet(value) => {
                        let matching_hypernet: Option<(char, char, char)> = abas
                            .iter()
                            .map(|aba| self.aba_to_bab(*aba))
                            .find(|bab| self.contains_bab(value, bab));

                        match matching_hypernet {
                            Some(_) => true,
                            None => false
                        }
                    }
                    IPv7Segment::Supernet(_) => false
                }
            });

        match supports_ssl {
            Some(_) => true,
            None => false
        }
    }

    pub fn extract_abas(&self, sequence: &IPv7Segment) -> Vec<(char, char, char)> {
        match sequence {
            IPv7Segment::Supernet(value) => self.extract_abas_from_value(&value),
            _ => vec![]
        }
    }

    pub fn extract_abas_from_value(&self, s: &Vec<char>) -> Vec<(char, char, char)> {
        let mut abas = vec![];
        let len = s.len();
        for i in 0..len - 2 {
            if s[i] == s[i + 2] && s[i] != s[i + 1] {
                abas.push((s[i], s[i + 1], s[i + 2]));
            }
        }

        abas
    }

    pub fn aba_to_bab(&self, s: (char, char, char)) -> (char, char, char) {
        (s.1, s.0, s.1)
    }

    pub fn contains_bab(&self, s: &Vec<char>, bab: &(char, char, char)) -> bool {
        let len = s.len();
        for i in 0..len - 2 {
            if s[i] == bab.0 && s[i + 1] == bab.1 && s[i + 2] == bab.2 {
                return true;
            }
        }
        false
    }

    pub fn contains_abba(s: &Vec<char>) -> bool {
        let len = s.len();
        for i in 0..len - 3 {
            if s[i] == s[i + 3] && s[i + 1] == s[i + 2] && s[i] != s[i + 1] {
                return true;
            }
        }
        false
    }
}

impl FromStr for IPv7 {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let mut sequences: Vec<IPv7Segment> = vec![];
        let mut in_hypernet = false;
        let mut current_sequence: Vec<char> = vec![];
        for ch in s.chars() {
            if ch == '[' {
                in_hypernet = true;
                if current_sequence.len() > 0 {
                    sequences.push(IPv7Segment::Supernet(current_sequence.clone()));
                    current_sequence.clear();
                }
            } else if ch == ']' {
                in_hypernet = false;
                if current_sequence.len() > 0 {
                    sequences.push(IPv7Segment::Hypernet(current_sequence.clone()));
                    current_sequence.clear();
                }
            } else {
                current_sequence.push(ch);
            }
        }

        if current_sequence.len() > 0 {
            sequences.push(
                match in_hypernet {
                    true => IPv7Segment::Hypernet(current_sequence.clone()),
                    false => IPv7Segment::Supernet(current_sequence.clone())
                }
            );
        }

        Ok(IPv7::new(sequences))
    }
}