use crate::day22::node::Node;
use std::str::FromStr;
use itertools::{Itertools};
use std::collections::{HashMap, HashSet};
use pathfinding::directed::bfs::bfs;

#[derive(Debug, Eq, PartialEq)]
pub struct Cluster {
    nodes: HashMap<(u8, u8), Node>
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
                    let (_, a) = items.get(0).unwrap();
                    let (_, b) = items.get(1).unwrap();

                    !a.is_same_node(&b) &&
                        ((!a.is_empty() && a.fits_on(&b)) || (!b.is_empty() && b.fits_on(&a)))
                }
            })
            .collect::<Vec<Vec<(&(u8, u8), &Node)>>>()
            .len() as u32
    }

    /// The idea here is to determine the maximum (x, y) coordinates to work out our grid space,
    /// then find out the total number of other nodes that each node could theoretically move its
    /// data into (if the other nodes were empty) and then find the outliers, which we consider to
    /// be walls.
    ///
    /// Now, given an effective grid with obstacles/walls, a empty node/space and a target node
    /// containing the data we're interested in, we can use BFS to find the shortest path to the
    /// node which is located 1 step to the left of the target. We then use a pattern of...
    ///
    /// - swap the space with the target data
    /// - move the space down
    /// - move the space left
    /// - move the space again
    /// - and move the space up so it once again ends up to the left of the target data
    ///
    /// and work out that we need 1 move for an initial swap and 4 more moves to reposition the
    /// space to the left of the target data. We do this repeatedly until the target data is at
    /// (0, 0).
    ///
    /// Adding the length of the shortest path we found to this value, gives us the minimum number
    /// of moves to get the target data across to the beginning using the empty space.
    ///
    pub fn minimum_steps_to_move_target_data_to_start(&self) -> u32 {
        let max_key = self.find_max_key();
        let target_key = (max_key.0, 0);
        let empty_key = self.find_empty_key();
        let walls = self.find_walls();

        let path = bfs(
            &empty_key,
            |node|
                self.find_neighbours_of_key(node, &max_key)
                    .into_iter()
                    .filter(|node| !walls.contains(node))
                    .collect::<Vec<(u8, u8)>>(),
            |node| node == &(target_key.0 - 1, target_key.1)
        );

        // Length of shortest path to move the space to the node one point to the left of the target
        let length_to_left_of_target = (path.unwrap().len() as u32) - 1;

        // The number of moves required to move the target data across to the beginning, using the
        // space. The idea here is to swap the space with the target (which is to the right of the
        // space) which is 1 move, then to move the space down, left, left, up to get it once again
        // to the left of the target data which is a total of 5 moves to move the target data one
        // node to the left and reposition the space to the left for the next move. We don't need to
        // reposition the space at the end since that initial swap of the space and the target data
        // will be the last move we make, so we can subtract 1 from the target key's X coord,
        // multiply that value by 5 and then simply add 1 for the final swap.
        let length_to_move_target_across = (((target_key.0 as u32) - 1) * 5) + 1;

        length_to_left_of_target + length_to_move_target_across
    }

    fn find_max_key(&self) -> (u8, u8) {
        let mut max_x = 0;
        let mut max_y = 0;
        self.nodes.iter().for_each(|(&key, _)| {
            if key.0 > max_x {
                max_x = key.0;
            }

            if key.1 > max_y {
                max_y = key.1;
            }
        });

        return (max_x, max_y);
    }

    fn find_empty_key(&self) -> (u8, u8) {
        self.nodes
            .iter()
            .map(|(_, node)| node)
            .find(|node| node.is_empty())
            .unwrap()
            .get_key()
    }

    /// Find the outliers in the dataset, i.e. those nodes which have significantly higher
    /// capacities and hold significantly more data than the other nodes in the cluster. By doing
    /// this, we can determine which coordinates in our cluster represent effective walls/obstacles
    /// that we need to avoid when using BFS to path-find our way to move the space to the node one
    /// coordinate to the left of the target.
    fn find_walls(&self) -> HashSet<(u8, u8)> {
        // calculate how many other nodes that each node's data would fit into if all other target
        // nodes were empty. Outliers (walls) will fit into significantly fewer nodes due to their
        // significantly larger data size compared to the majority of nodes (explained in challenge
        // text). In my case, I found that the vast majority of nodes fit into 1020 other nodes and
        // the outliers only fit into 26 other nodes (other outliers).
        let total_possible_targets = self.nodes
            .iter()
            .map(|(key, node)| {
                let targets = self.nodes
                    .iter()
                    .filter(|&(_, other)| node.fits_on_empty(other))
                    .collect::<Vec<(&(u8, u8), &Node)>>()
                    .len();
                (*key, targets)
            })
            .collect::<HashMap<(u8, u8), usize>>();

        // Next we attempt to detect the outliers, by first sorting the totals achieved (ignoring
        // the points to which they refer), detecting an upper and lower quartile of that dataset
        // and using it to generate an outer fence. The outer fence is an inclusive range such that
        // any datum that does not fit into it, is considered an outlier and in our case, a wall.
        let sorted_targets: Vec<usize> = total_possible_targets
            .iter()
            .map(|(_, targets)| *targets)
            .sorted()
            .collect();
        let lq = sorted_targets.get(sorted_targets.len() / 4).unwrap();
        let uq = sorted_targets.get(sorted_targets.len() / 4 * 3).unwrap();
        let iqr = uq - lq;
        let of = (lq + (iqr * 3))..=(uq + (iqr * 3));

        // Finally, we iterate through our dataset which maps points to the total other nodes their
        // data would fit into and use those totals to determine whether the point is an
        // outlier/wall, i.e. does not fit within our outer range. We collect these points into a
        // hashset and return them, discarding the totals now that we're done with them.
        total_possible_targets
            .iter()
            .filter(|(_, targets)| !of.contains(targets))
            .map(|(key, _)| *key)
            .collect::<HashSet<(u8, u8)>>()
    }

    fn find_neighbours_of_key(&self, node: &(u8, u8), max: &(u8, u8)) -> Vec<(u8, u8)> {
        let max = *max;
        let max_x = max.0 as i16;
        let max_y = max.1 as i16;
        vec![(1i16, 0i16), (-1i16, 0i16), (0i16, 1i16), (0i16, -1i16)]
            .into_iter()
            .map(|delta| {
                let node = *node;
                let x = (node.0 as i16) + delta.0;
                let y = (node.1 as i16) + delta.1;
                if x < 0 || y < 0 || x > max_x || y > max_y {
                    None
                } else {
                    Some((x as u8, y as u8))
                }
            })
            .filter(|node| node.is_some())
            .map(|node| node.unwrap())
            .collect()
    }
}

impl FromStr for Cluster {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let mut nodes = HashMap::new();
        s.lines()
            .skip(2)
            .for_each(|line| {
                let node: Node = line.parse().unwrap();
                nodes.insert(node.get_key(), node);
            });
        Ok(Cluster { nodes })
    }
}

#[cfg(test)]
mod test {
    use crate::day22::cluster::Cluster;
    use crate::day22::node::Node;
    use std::collections::HashMap;

    #[test]
    fn from_str() {
        let mut nodes = HashMap::new();
        nodes.insert((0, 0), Node::new(0, 0, 93, 71, 22));
        nodes.insert((0, 1), Node::new(0, 1, 93, 65, 28));
        nodes.insert((1, 0), Node::new(1, 0, 86, 71, 15));
        nodes.insert((1, 1), Node::new(1, 1, 94, 73, 21));

        let expected = Cluster { nodes };
        let actual = "root@ebhq-gridcenter# df -h\n\
                     Filesystem              Size  Used  Avail  Use%\n\
                     /dev/grid/node-x0-y0     93T   71T    22T   76%\n\
                     /dev/grid/node-x0-y1     93T   65T    28T   69%\n\
                     /dev/grid/node-x1-y0     86T   71T    15T   82%\n\
                     /dev/grid/node-x1-y1     94T   73T    21T   77%".parse::<Cluster>().unwrap();
        assert_eq!(expected, actual);
    }
}
