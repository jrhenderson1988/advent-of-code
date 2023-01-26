from .common import single_differing_index


def run(content):
    lines = content.strip().splitlines()
    for i in range(0, len(lines)):
        a = lines[i].strip()
        for b in lines[i + 1:]:
            b = b.strip()
            index = single_differing_index(a, b)
            if index is not False:
                return a[:index] + a[index + 1:]

    return 'Nothing :('
