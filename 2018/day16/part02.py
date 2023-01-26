from .common import SampleProcessor, Program


def run(content):
    data = content.strip().splitlines()

    sample_processor = SampleProcessor.parse(data)
    program = Program.parse(data)

    program.run(sample_processor.discover_op_codes())

    return program.get_final_state()[0]
