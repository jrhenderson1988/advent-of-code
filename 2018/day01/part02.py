def run(content):
    seen = {0}
    frequency = 0
    while True:
        for line in content.strip().splitlines():
            frequency += int(line)
            if frequency in seen:
                return frequency
            else:
                seen.add(frequency)
