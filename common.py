def load_input(path, trim=True):
    input = []
    with open(path) as f:
        for line in f:
            input.append(line.strip() if trim else line)
    return input