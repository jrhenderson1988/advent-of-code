# require all the days
Dir[File.dirname(__FILE__) + '/day*.rb'].each do |path|
  require(path)
end

module Aoc
  class Application
    def run(args)
      if args.length == 0
        (1..25).each do |day|
          execute_day_and_part(day, 1)
          execute_day_and_part(day, 2)
        end
        0
      elsif args.length == 1
        day = args[0].to_i
        execute_day_and_part(day, 1)
        execute_day_and_part(day, 2)
        0
      elsif args.length == 2
        day = args[0].to_i
        part = args[1].to_i
        execute_day_and_part(day, part)
        0
      else
        puts("expected 0, 1 or 2 arguments")
        1
      end
    end

    def execute_day_and_part(day, part)
      class_name = "Aoc::Day#{day.to_s.rjust(2, "0")}"
      method_name = "part#{part}"
      input = File.read(File.dirname(File.dirname(File.dirname(__FILE__))) + "/input/#{day}.in")
      clazz = Object.const_get(class_name)
      instance = clazz.new(input, false) # false = not a test

      start = Time.now
      actual = instance.send(method_name).to_s
      finish = Time.now
      duration = ((finish - start) * 1000.0).to_i
      puts("#{class_name}##{method_name} (#{duration}ms): #{actual}")
    end
  end
end