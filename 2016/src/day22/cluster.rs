use crate::day22::node::Node;
use std::str::FromStr;
use itertools::Itertools;

#[derive(Debug, Eq, PartialEq)]
pub struct Cluster {
    nodes: Vec<Node>
}

impl Cluster {
    pub fn total_viable_pairs(&self) -> u32 {
        self.nodes
            .iter()
            .combinations(2)
            .filter(|items| {
                if items.len() != 2 {
                    false
                } else {
                    let a = items.get(0).unwrap();
                    let b = items.get(1).unwrap();

                    !a.is_same_node(&b) &&
                        ((!a.is_empty() && a.fits_on(&b)) || (!b.is_empty() && b.fits_on(&a)))
                }
            })
            .collect::<Vec<Vec<&Node>>>()
            .len() as u32
    }
}

impl FromStr for Cluster {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let nodes = s.lines().skip(2).map(|line| line.parse::<Node>().unwrap()).collect();
        Ok(Cluster { nodes })
    }
}

#[cfg(test)]
mod test {
    use crate::day22::cluster::Cluster;
    use crate::day22::node::Node;

    #[test]
    fn from_str() {
        let expected = Cluster {
            nodes: vec![
                Node::new(0, 0, 93, 71, 22),
                Node::new(0, 1, 93, 65, 28),
                Node::new(1, 0, 86, 71, 15),
                Node::new(1, 1, 94, 73, 21)
            ]
        };
        let actual = "root@ebhq-gridcenter# df -h\n\
                     Filesystem              Size  Used  Avail  Use%\n\
                     /dev/grid/node-x0-y0     93T   71T    22T   76%\n\
                     /dev/grid/node-x0-y1     93T   65T    28T   69%\n\
                     /dev/grid/node-x1-y0     86T   71T    15T   82%\n\
                     /dev/grid/node-x1-y1     94T   73T    21T   77%".parse::<Cluster>().unwrap();
        assert_eq!(expected, actual);
    }
}
