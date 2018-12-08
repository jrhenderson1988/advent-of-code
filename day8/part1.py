from loader import load_input_as_string
import os


class Node:
    def __init__(self, children: list, metadata: list):
        self.children = children
        self.metadata = metadata

    def __repr__(self):
        s = 'Node(%s)' % (':'.join([str(m) for m in self.metadata]))
        if len(self.children) > 0:
            s += ' { %s }' % ', '.join([str(n) for n in self.children])

        return s

    def __str__(self):
        return self.__repr__()

    def calculate_checksum(self):
        checksum = sum(self.metadata)
        for child in self.children:
            checksum += child.calculate_checksum()

        return checksum

    @staticmethod
    def load_children(data: list, num_children: int):
        nodes = []

        for i in range(0, num_children):
            children = data[0]
            metadata = data[1]

            if children == 0:
                nodes.append(Node([], data[2:2 + metadata]))
                data = data[2 + metadata:]
            else:
                grandchildren, data = Node.load_children(data[2:], children)
                child = Node(grandchildren, data[:metadata])
                nodes.append(child)

        return nodes, data

    @staticmethod
    def load(data: list):
        if len(data) < 2:
            raise ValueError('Invalid data provided')

        children, _ = Node.load_children(data[2:-data[1]], data[0])

        return Node(children, data[-data[1]:])


def run():
    data = [int(d) for d in load_input_as_string(os.path.join(os.path.dirname(__file__), 'input.txt')).split(' ')]
    # data = [1, 1, 0, 2, 3, 6, 99]
    # data = [2, 1, 0, 2, 3, 6, 0, 1, 56, 99]
    # data = [0, 2, 3, 6]

    tree = Node.load(data)
    print(tree)

    return tree.calculate_checksum()
