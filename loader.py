def load_input(path, trim=True):
    input = []
    with open(path) as f:
        for line in f:
            input.append(line.strip() if trim else line)
    return input

def load_input_as_string(path, trim=True):
    input = None
    with open(path) as f:
        input = f.read()
        return input if not trim else input.strip()
