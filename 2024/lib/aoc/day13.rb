module Aoc
  class Day13 < Day
    MAX_ATTEMPTS_PER_BUTTON = 10000

    def part1
      rules.map { |rule| fewest_tokens_to_win_prize_smart(rule) }
           .reject { |cost| cost.nil? }
           .sum
    end

    def part2
      rules.map { |rule| recalculate_prize_position(rule) }
           .map { |rule| fewest_tokens_to_win_prize_smart(rule) }
           .reject { |cost| cost.nil? }
           .sum
    end

    def rules
      @rules ||=
        content.gsub(/\r/, "\n")
               .split(/\n{2,}/)
               .map { |chunk| parse_rule(chunk.strip) }
    end

    def parse_rule(chunk)
      button_a_captures = chunk.match(/Button A: X\+(\d+), Y\+(\d+)/).captures
      button_b_captures = chunk.match(/Button B: X\+(\d+), Y\+(\d+)/).captures
      prize_captures = chunk.match(/Prize: X=(\d+), Y=(\d+)/).captures

      [
        button_a_captures.map { |c| c.to_i },
        button_b_captures.map { |n| n.to_i },
        prize_captures.map { |i| i.to_i }
      ]
    end

    def fewest_tokens_to_win_prize_naive(rule)
      a, b, prize = rule
      a_x, a_y = a
      b_x, b_y = b
      prize_x, prize_y = prize

      lowest_cost = nil
      (0..100).each { |button_a_presses|
        (0..100).each { |button_b_presses|
          actual_x = (button_a_presses * a_x) + (button_b_presses * b_x)
          actual_y = (button_a_presses * a_y) + (button_b_presses * b_y)

          if matches([actual_x, actual_y], [prize_x, prize_y])
            cost = cost_of_a_tokens(button_a_presses) + cost_of_b_tokens(button_b_presses)
            if lowest_cost.nil? || cost < lowest_cost
              lowest_cost = cost
            end
          end
        }
      }

      lowest_cost
    end

    def fewest_tokens_to_win_prize_smart(rule)
      # fewest_tokens_to_win_prize_naive(rule)

      a, b, prize = rule
      a_x, a_y = a
      b_x, b_y = b
      prize_x, prize_y = prize

      max_b_presses = [(prize_x / b_x), (prize_y / b_y)].min
      # max_a_presses = [(prize_x / a_x), (prize_y / a_y)].min
      max_a_presses = MAX_ATTEMPTS_PER_BUTTON # set an arbitrary upper limit before we decide it's not possible
      min_b_presses = [max_b_presses - MAX_ATTEMPTS_PER_BUTTON, 0].max
      for button_b_presses in (max_b_presses).downto(min_b_presses)
        for button_a_presses in (0..max_a_presses)
          actual_x = (button_a_presses * a_x) + (button_b_presses * b_x)
          actual_y = (button_a_presses * a_y) + (button_b_presses * b_y)

          if matches([actual_x, actual_y], prize)
            return cost_of_a_tokens(button_a_presses) + cost_of_b_tokens(button_b_presses)
          end

          break if exceeds([actual_x, actual_y], prize)
        end
      end
      # puts("#{rule.inspect} -> #{max_b_presses}")

      # SMARTER_WAY
      # a costs 3 tokens
      # b costs 1 token
      # prioritise pressing b (cost 1 token)
      # work out the most amount of times we can press B and either hit, or stay below the totals
      # iterate down, removing a B button press and continue pressing A until we hit the totals or we exceed in any way
      # when we eventually hit the totals, return the number of tokens
      # if we never hit the totals
      nil
    end

    def recalculate_prize_position(rule)
      a, b, prize = rule
      prize_x, prize_y = prize
      new_prize = [prize_x + 10000000000000, prize_y + 10000000000000]
      [a, b, new_prize]
    end

    def cost_of_a_tokens(number)
      number * 3
    end

    def cost_of_b_tokens(number)
      number
    end

    def exceeds(score, target)
      score_x, score_y = score
      target_x, target_y = target

      score_x > target_x || score_y > target_y
    end

    def matches(score, target)
      score == target
    end
  end
end
