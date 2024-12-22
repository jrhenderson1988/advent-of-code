module Aoc
  class Day22 < Day
    def part1
      initial_secret_numbers.map { |sn| nth_secret_number(2000, sn) }.sum
    end

    def part2
      all_sale_configurations =
        initial_secret_numbers.map { |sn| build_sale_configuration_for_buyer(sn) }

      merged_sale_configurations = merge_sale_configurations(all_sale_configurations)

      merged_sale_configurations.map { |_, price| price }.max
    end

    def nth_secret_number(n, initial_secret_number)
      n_secret_numbers(n, initial_secret_number).last
    end

    def n_secret_numbers(n, initial_secret_number)
      (0...n).reduce([initial_secret_number]) { |acc, _| acc + [next_secret_number(acc.last)] }
    end

    def next_secret_number(prev)
      step1 = prune(mix(prev, mul64(prev)))
      step2 = prune(mix(step1, div32(step1)))

      prune(mix(step2, mul2048(step2)))
    end

    def build_sale_configuration_for_buyer(buyer_secret_number)
      prices = n_secret_numbers(2000, buyer_secret_number).map { |sn| to_price(sn) }

      sale_configuration = {}

      (1...prices.length - 3)
        .map { |i| [price_configuration_at(prices, i), prices[i + 3]] }
        .each { |pair|
          configuration, sale_price = pair
          if sale_price.nil?
            raise ArgumentError, "sale price is nil"
          end

          if sale_configuration[configuration].nil?
            # only take the first price we see with that configuration
            sale_configuration[configuration] = sale_price
          end
        }

      sale_configuration
    end

    def merge_sale_configurations(sale_configurations)
      merged = {}

      sale_configurations.each do |sale_configuration|
        sale_configuration.each { |configuration, price|
          merged[configuration] = (merged[configuration] || 0) + price
        }
      end

      merged
    end

    def price_configuration_at(prices, i)
      [
        prices[i] - prices[i - 1],
        prices[i + 1] - prices[i],
        prices[i + 2] - prices[i + 1],
        prices[i + 3] - prices[i + 2],
      ]
    end

    def initial_secret_numbers
      @initial_secret_numbers ||= lines.map { |line| line.strip.to_i }
    end

    def mix(secret_number, value)
      secret_number ^ value
    end

    def prune(value)
      value % 16777216
    end

    def mul64(secret_number)
      secret_number * 64
    end

    def mul2048(secret_number)
      secret_number * 2048
    end

    def div32(secret_number)
      secret_number / 32
    end

    def to_price(secret_number)
      secret_number.to_s.chars.last.to_i
    end
  end
end