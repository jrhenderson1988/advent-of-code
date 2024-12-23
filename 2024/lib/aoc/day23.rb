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

    def connections
      @connections ||= lines.map { |line| parse_connection(line.strip) }
    end

    def connection_set
      @connection_set ||= connections.to_set
    end

    def triplets
      (0...connections.length)
        .flat_map { |i|
          (i + 1...connections.length).map {
            |j| connections[i].union(connections[j])
          }
        }.filter { |union| union.length == 3 }
    end

    def triplet_ring?(triplet)
      a, b, c = triplet.to_a
      member?(Set[a, b]) && member?(Set[b, c]) && member?(Set[a, c])
    end

    def lan_party_password
      computers = connections.flat_map { |pair| [pair.to_a[0], pair.to_a[1]] }.to_set.to_a
      networks = computers.map { |cpu| Set[cpu] }

      (0...networks.length).each do |i|
        network = networks[i]
        computers.each { |computer|
          if network.all? { |existing| connections.member?(Set[existing, computer]) }
            network.add(computer)
          end
        }
        networks[i] = network
      end

      biggest_lan = networks.max_by { |l| l.length }
      password(biggest_lan)
    end

    def password(lan)
      lan.to_a.sort.join(",")
    end

    def member?(val)
      connection_set.member?(val)
    end

    def parse_connection(line)
      match = line.match(/^([a-z]{2})-([a-z]{2})$/)
      if match.nil?
        raise ArgumentError, "invalid line"
      end
      first, second = match.captures
      Set[first, second]
    end
  end
end