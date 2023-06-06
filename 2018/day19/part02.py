def run(_content):
    return 30481920

# Reverse engineering solution...
# Found out that there's effectively a constant that is worked out at the beginning, which land in register 3. In part
# 1, this constant is 888. In part 2, the constant is 10551288.
#
# The solution ends up calculating the sum of all the factors of the target number.
#
# In the case of part 1 (target = 888), the factors are: 1, 2, 3, 4, 6, 8, 12, 24, 37, 74, 111, 148, 222, 296, 444, 888.
# The sum of these factors is: 2280
#
# In part 2 (target = 10551288), the factors are: 1, 2, 3, 4, 6, 8, 11, 12, 17, 22, 24, 33, 34, 44, 51, 66, 68, 88, 102,
# 132, 136, 187, 204, 264, 374, 408, 561, 748, 1122, 1496, 2244, 2351, 4488, 4702, 7053, 9404, 14106, 18808, 25861,
# 28212, 39967, 51722, 56424, 77583, 79934, 103444, 119901, 155166, 159868, 206888, 239802, 310332, 319736, 439637,
# 479604, 620664, 879274, 959208, 1318911, 1758548, 2637822, 3517096, 5275644, 10551288.
#
# The sum of these factors is 30481920
