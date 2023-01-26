from .common import Node


def run(content):
    data = [int(d) for d in content.split(' ')]

    tree = Node.load(data)

    return tree.calculate_checksum()
