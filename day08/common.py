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

    def calculate_value(self):
        if len(self.children) == 0:
            return sum(self.metadata)

        value = 0
        for meta in self.metadata:
            if meta < 1 or meta > len(self.children):
                continue

            value += self.children[meta - 1].calculate_value()

        return value

    @staticmethod
    def load_children(data: list, num_children: int):
        nodes = []

        for _ in range(num_children):
            children = data[0]
            metadata = data[1]

            if children == 0:
                node = Node([], data[2:2 + metadata])
                nodes.append(node)
                data = data[2 + metadata:]
            else:
                grandchildren, data = Node.load_children(data[2:], children)
                child = Node(grandchildren, data[:metadata])
                data = data[metadata:]
                nodes.append(child)

        return nodes, data

    @staticmethod
    def load(data: list):
        if len(data) < 2:
            raise ValueError('Invalid data provided')

        children, _ = Node.load_children(data[2:-data[1]], data[0])

        return Node(children, data[-data[1]:])
