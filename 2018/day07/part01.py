from .common import DependencyTree


def run(content):
    tree = DependencyTree.parse(content.strip().splitlines())
    return tree.resolve_path()
