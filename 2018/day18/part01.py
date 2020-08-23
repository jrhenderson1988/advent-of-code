from loader import load_input
from .common import Area
import os


def run():
    data = load_input(os.path.join(os.path.dirname(__file__), 'input.txt'))

    area = Area.parse(data)

    return area.get_total_resource_value_after(10)
