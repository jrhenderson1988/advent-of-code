def run(content):
    frequency = 0
    for line in content.strip().splitlines(keepends=False):
        frequency += int(line)

    return frequency
