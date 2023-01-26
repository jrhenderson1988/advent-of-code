from .common import DependencyTree


def run(content, workers=5, offset=60):
    tree = DependencyTree.parse(content.strip().splitlines())
    path, time = tree.resolve_for_workers(workers, offset)
    return time
