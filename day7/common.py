from re import compile


class DependencyTree:
    def __init__(self):
        self.tree = {}

    def add(self, node: str, dependency: str):
        for key in [node, dependency]:
            if key not in self.tree:
                self.tree[key] = set()

        self.tree[node].add(dependency)

    @staticmethod
    def get_next_node(tree: dict):
        node = None
        for n in tree:
            if len(tree[n]) == 0 and (node is None or node > n):
                node = n

        return node

    @staticmethod
    def remove_node_from_tree(tree: dict, node: str):
        del tree[node]
        for n in tree:
            if node in tree[n]:
                tree[n].remove(node)

        return tree

    def resolve_path(self):
        path = ''
        tree = self.tree
        while len(tree) > 0:
            node = DependencyTree.get_next_node(tree)
            tree = DependencyTree.remove_node_from_tree(tree, node)
            path += node

        return path

    def __repr__(self):
        return '{' + ', '.join(['%s: [%s]' % (n, ', '.join(d)) for n, d in self.tree.items()]) + '}'

    def __str__(self):
        return self.__repr__()

    @staticmethod
    def parse(lines):
        tree = DependencyTree()
        for line in lines:
            pattern = compile(r'^Step (?P<dependency>[A-Z]) must be finished before step (?P<node>[A-Z]) can begin\.$')
            match = pattern.match(line)
            if not match:
                raise ValueError('Invalid line')

            tree.add(match.group('node'), match.group('dependency'))

        return tree
