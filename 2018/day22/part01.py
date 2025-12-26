from day22.common import CaveSystem


def run(content):
    cs = CaveSystem.parse(content)
    # print(cs.erosion_level((0, 0)))
    # print(cs.erosion_level((1, 0)))
    # print(cs.erosion_level((0, 1)))
    # print(cs.erosion_level((1, 1)))
    # print(cs.erosion_level(cs.target))

    return cs.total_risk_factor_for_initial_area()