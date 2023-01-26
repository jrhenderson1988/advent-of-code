from .common import Area


def run(content):
    data = content.strip().splitlines()

    area = Area.parse(data)

    return area.get_total_resource_value_after(1000000000)
