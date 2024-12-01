require_relative '../spec_helper'

def class_exists?(class_name)
  begin
    klass = Kernel.const_get(class_name)
    klass.is_a?(Class)
  rescue NameError
    false
  end
end

Dir[File.dirname(__FILE__) + "/data/*.out"].each do |output_path|
  name = output_path[output_path.rindex("/") + 1..output_path.rindex(".out") - 1]
  day, part = name.match(/d(\d\d)p([12]).*/).captures
  input_path = output_path[0..output_path.rindex("/")] + "d#{day}.in"
  class_name = "Aoc::Day#{day}"
  method_name = "part#{part}"

  describe "#{name} (#{day} / #{part})" do
    it "#{class_name}##{method_name}" do
      input = File.read(input_path)
      expected = File.read(output_path)

      clazz = Object.const_get(class_name)
      instance = clazz.new(input)
      actual = instance.send("part#{part}").to_s

      expect(actual).to eq(expected)
    end
  end
end

# (1..25).each { |day|
#   padded_day = day.to_s.rjust(2, "0")
#   class_name = "Aoc::Day#{padded_day}"
#   describe "#{class_name}" do
#
#     if class_exists?(class_name)
#       (1..2).each { |part|
#         context "#part#{part}" do
#           inputs = Dir[File.dirname(__FILE__) + "/data/d#{padded_day}p#{part}*.in"]
#           if inputs.length > 0
#             inputs.each { |input_filename|
#               output_filename = input_filename.sub(".in", ".out")
#
#               it "(#{File.basename(input_filename)})" do
#                 input = File.read(input_filename)
#                 expected = File.read(output_filename) if File.file?(output_filename)
#                 fail("no corresponding output file for input #{input_filename}") if expected.nil?
#
#                 clazz = Object.const_get(class_name)
#                 instance = clazz.new(input)
#                 actual = instance.send("part#{part}").to_s
#
#                 expect(actual).to eq(expected)
#               end
#             }
#           else
#             it "works" do
#               skip("no input files")
#             end
#           end
#
#         end
#       }
#     end
#   end
# }
