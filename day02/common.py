def get_occurrences(line):
    occ = {}
    for c in line:
        occ[c] = 1 if c not in occ else occ[c] + 1
    return occ


def exactly_x(occurrences, count):
    for t in occurrences.values():
        if t is count:
            return True
    return False


def single_differing_index(a, b):
    index = False
    for i in range(0, len(a)):
        if a[i] != b[i]:
            if index is False:
                index = i
            else:
                return False
    return index
