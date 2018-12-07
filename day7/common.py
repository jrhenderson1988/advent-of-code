from re import compile
from string import ascii_uppercase


class DependencyTree:
    def __init__(self):
        self.tree = {}

    def add(self, node: str, dependency: str):
        for key in [node, dependency]:
            if key not in self.tree:
                self.tree[key] = set()

        self.tree[node].add(dependency)

    @staticmethod
    def get_duration(node: str, offset: int):
        duration = ascii_uppercase.find(node.upper())
        if duration < 0:
            raise ValueError('Cannot determine duration for node %s' % node)

        return offset + duration + 1

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

    def resolve_for_workers(self, num_workers, duration_offset):
        workers = {i: None for i in range(0, num_workers)}
        jobs = self.tree
        path = []

        if len(workers) < 1:
            raise ValueError('Not enough workers')

        elapsed = -1
        while len(jobs) > 0 or len([w for w in workers if workers[w] is not None]) > 0:
            elapsed += 1

            # Check the progress of each active worker, determine if their job is completed, if so, make the worker
            # available again and remove the job from the dependency lists of other jobs. This should happen before we
            # do anything else to ensure that a worker can immediately finish a job and begin with another.
            for i, status in workers.items():
                if status is not None:
                    job = status[0]
                    started = status[1]
                    duration = DependencyTree.get_duration(job, duration_offset)
                    if elapsed >= started + duration:
                        path.append(job)
                        workers[i] = None
                        for j in [j for j in jobs.keys() if job in jobs[j]]:
                            jobs[j].remove(job)

            # Get the available workers and jobs at this point in time. If there aren't any available jobs or there
            # aren't any available workers, then we can simply continue with the next iteration.
            available_workers = [i for i in workers.keys() if workers[i] is None]
            available_jobs = [j for j in jobs if len(jobs[j]) == 0]
            if len(available_workers) == 0 or len(available_jobs) == 0:
                continue

            # Assign available jobs to available workers. Assign a tuple containing the job and the start time to the
            # worker and remove the job from the job list (But not from the dependency lists of other jobs).
            for i in range(0, (min(len(available_jobs), len(available_workers)))):
                job = available_jobs[i]
                worker = available_workers[i]
                workers[worker] = (job, elapsed)
                del jobs[job]

        return path, elapsed





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
