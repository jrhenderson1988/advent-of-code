def reduce(s):
    i = 0
    length = len(s)
    while i < length - 1: 
        while length > 1 and i < length - 1 and s[i] != s[i+1] and s[i].lower() == s[i+1].lower():
            s = s[:i] + s[i+2:]
            length -= 2
            i = max(0, i - 1)
        i += 1

    return s