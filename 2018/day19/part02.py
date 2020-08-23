def run():
    return 24619952


# I could not get a working solution for part 2 so I manually worked out what was happening by monitoring
# the output of each instruction and then by reverse engineering each instruction. I ended up translating
# the instructions into a super-inefficient algorithm that worked out all of the factors of a large number
# and then added them together to give the result. Below shows how I got from the instructions in my input
# to my resulting algorithm in order to see what was happening and eventually find out what was required.
#
#
#
#
# 00: r2 = r2 + 16
# 01: r1 = 1
# 02: r5 = 1
# 03: r4 = r1 * r5
# 04: r4 = r4 == r3 ? 1 : 0
# 05: r2 = r4 + r2
# 06: r2 = r2 + 1
# 07: r0 = r1 + r0
# 08: r5 = r5 + 1
# 09: r4 = r5 > b3 ? 1 : 0
# 10: r2 = r2 + r4
# 11: r2 = 2
# 12: r1 = r1 + 1
# 13: r4 = r1 > b3 ? 1 : 0
# 14: r2 = r4 + r2
# 15: r2 = 1
# 16: r2 = r2 * r2
# 17: r3 = r3 + 2
# 18: r3 = r3 * r3
# 19: r3 = r2 * r3
# 20: r3 = r3 * 11
# 21: r4 = r4 + 7
# 22: r4 = r4 * r2
# 23: r4 = r4 + 6
# 24: r3 = r3 + r4
# 25: r2 = r2 + r0
# 26: r2 = 0
# 27: r4 = r2
# 28: r4 = r4 * r2
# 29: r4 = r2 + r4
# 30: r4 = r2 * r4
# 31: r4 = r4 * 14
# 32: r4 = r4 * r2
# 33: r3 = r3 + r4
# 34: r0 = 0
# 35: r2 = 0
#
#
#
#
# 00: r2 = GOTO 16
# 01: r1 = 1
# 02: r5 = 1
# 03: r4 = r1 * r5
# 04: r4 = r4 == r3 ? 1 : 0
# 05: r2 = r4 + 5
# 06: r2 = GOTO 7
# 07: r0 = r1 + r0
# 08: r5 = r5 + 1
# 09: r4 = r5 > b3 ? 1 : 0
# 10: r2 = 10 + r4
# 11: r2 = GOTO 2
# 12: r1 = r1 + 1
# 13: r4 = r1 > b3 ? 1 : 0
# 14: r2 = r4 + 14
# 15: r2 = GOTO 1
# 16: r2 = GOTO 256 (end)
# 17: r3 = r3 + 2
# 18: r3 = r3 * r3
# 19: r3 = 19 * r3
# 20: r3 = r3 * 11
# 21: r4 = r4 + 7
# 22: r4 = r4 * 22
# 23: r4 = r4 + 6
# 24: r3 = r3 + r4
# 25: r2 = 25 + r0
# 26: r2 = GOTO 0
# 27: r4 = 27
# 28: r4 = r4 * 28
# 29: r4 = 29 + r4
# 30: r4 = 30 * r4
# 31: r4 = r4 * 14
# 32: r4 = r4 * 32
# 33: r3 = r3 + r4
# 34: r0 = 0
# 35: r2 = GOTO 0
#
#
#
#
# r0 = result
# r1 = operand A
# r2 = instruction pointer
# r3 = target
# r4 = condition / product
# r5 = operand B
#
#
#
#
# r3 = 10551396
# r0 = 0
# r1 = 1
# r5 = 1
# while True:
#     r4 = r1 * r5
#
#     if r4 == r3:
#         r0 += r1
#
#     r5 += 1
#
#     if r5 > r3:
#         r1 += 1
#     else:
#         continue
#
#     if r1 > r3:
#         return r0
#     else:
#         r5 = 1
#
#
#
#
# target = 10551396
# result = 0
# a = 1
# b = 1
# while True:
#     product = a * b
#
#     if product == target:
#         result += a
#
#     b += 1
#
#     if b > target:
#         a += 1
#     else:
#         continue
#
#     if a > target:
#         return result
#     else:
#         b = 1
#
# Factors of 10551396 are: 1, 2, 3, 4, 6, 12, 879283, 1758566, 2637849, 3517132, 5275698, 10551396
# Total of factors & Answer to part 2: 24619952
#
