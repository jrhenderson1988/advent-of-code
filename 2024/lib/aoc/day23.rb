require 'set'

module Aoc
  class Day23 < Day
    def part1
      three_node_rings_where_any_computer_starts_with_t
    end

    def part2
      lan_party_password
    end

    def triplet_rings
      triplets.filter { |triplet| triplet_ring?(triplet) }.to_set
    end

    def three_node_rings_where_any_computer_starts_with_t
      triplet_rings.filter { |ring| ring.filter { |cpu| cpu.start_with?("t") }.any? }.length
    end

    def computer_pairs
      @computer_pairs ||= lines.map { |line| parse_computers(line.strip) }
    end

    def computer_pair_set
      @computer_pair_set ||= computer_pairs.to_set
    end

    def triplets
      (0...computer_pairs.length)
        .flat_map { |i|
          (i + 1...computer_pairs.length).map {
            |j| computer_pairs[i].union(computer_pairs[j])
          }
        }.filter { |union| union.length == 3 }
    end

    def triplet_ring?(triplet)
      a, b, c = triplet.to_a
      member?(Set[a, b]) && member?(Set[b, c]) && member?(Set[a, c])
    end

    def lan_party_password
      computers = computer_pairs.flat_map { |pair| [pair.to_a[0], pair.to_a[1]] }
      connections = computer_pairs
      networks = computers.map { |cpu| Set[cpu] }

      (0...networks.length).each do |i|
        start = Time.now

        network = networks[i]
        # puts("#{i} -> #{network.inspect}")
        computers.each { |computer|
          # puts(" > #{computer}")
          if network.all? { |existing| connections.member?(Set[existing, computer]) }
            # puts("add...")
            network.add(computer)
          end
        }
        networks[i] = network

        finish = Time.now
        duration = ((finish - start) * 1000.0).to_i
        # puts("#{i} of #{networks.length} (#{duration}ms)")
      end

      # find the biggest lan
      biggest_lan = networks.max_by { |l| l.length }

      # get the lan password
      password(biggest_lan)
    end

    def password(lan)
      # find all the computers in the lan
      # re-order them alphabetically
      # return the comma separated string of computer names
      lan.to_a.sort.join(",")
    end

    def member?(val)
      computer_pair_set.member?(val)
    end

    def parse_computers(line)
      match = line.match(/^([a-z]{2})-([a-z]{2})$/)
      if match.nil?
        raise ArgumentError, "invalid line"
      end
      first, second = match.captures
      Set[first, second]
    end
  end
end