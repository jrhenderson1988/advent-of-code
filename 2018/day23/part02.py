from collections import defaultdict

from day23.common import Nanobot


# Inspired by this Rust solution:
# https://www.reddit.com/r/adventofcode/comments/a8s17l/comment/ecespv2/
# I honestly don't think I could have come up with something this clever.
def run(content):
    bots = [Nanobot.parse(line.strip()) for line in content.strip().splitlines()]

    dist = defaultdict(int)
    for bot in bots:
        d = bot.x + bot.y + bot.z
        dist[d - bot.r] += 1
        dist[d + bot.r + 1] -= 1
        # print("%s -> %s" % (bot, dist))

    sorted_dist = sorted(dist.items())
    # print("sorted", sorted_dist)

    cumulative_sums = []
    s = 0
    for d, x in sorted_dist:
        s += x
        cumulative_sums.append((d, s))
    # print("   run", run)

    max_sum = max(n for _, n in cumulative_sums)
    # print("maxsum", max_sum)

    intervals = []
    for i in range(len(cumulative_sums) - 1):
        (a, n), (b, _) = cumulative_sums[i], cumulative_sums[i + 1]
        if n == max_sum:
            intervals.append((a, b - 1))

    # print("intvls", intervals)
    return min([a for a, _ in intervals])
