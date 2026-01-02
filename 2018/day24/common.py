import re
from collections import defaultdict


class Battle:
    def __init__(self, groups):
        self.groups = groups

    @staticmethod
    def parse(content):
        chunks = re.split(r'(?:(?:\r?\n|\r)[ \t]*){2,}', content.strip())
        if len(chunks) != 2:
            raise Exception("Invalid input")

        groups = Battle.parse_army(chunks[0]) + Battle.parse_army(chunks[1])

        return Battle(groups)

    @staticmethod
    def parse_army(content):
        lines = content.strip().splitlines()
        army_name = lines[0].strip()[0:-1].lower().replace(' ', '_')
        return [Group.parse(lines[i], army_name, i) for i in range(1, len(lines))]

    def units_remaining_after_minimum_required_immune_system_boost(self):
        prev_boost = 0
        boost = 1000

        found_immune_system_win = False
        while True:
            print("boost: %d (prev boost: %d)" % (boost, prev_boost))
            curr_remaining, curr_winner = self.fight_with_immune_system_boost(boost)
            prev_remaining, prev_winner = self.fight_with_immune_system_boost(boost - 1)
            if curr_winner == 'immune_system' and prev_winner == 'infection':
                return curr_remaining

            if not found_immune_system_win and curr_winner == 'infection':
                # we have not yet found an immune system win, keep doubling the boost until we find one
                prev_boost = boost
                boost *= 2
            elif not found_immune_system_win and curr_winner == 'immune_system':
                # we've encountered an immune system win for the first time with ou doubling boosts, so now we have our
                # range in which to binary search (somewhere between the previous boost and current boost)
                print("found immune system at %d" % boost)
                found_immune_system_win = True
            else:
                # now we're in the binary search territory since we've found an immune system win, we need to keep
                # trying boosts between the current boost and the previous boost until we hit our exit condition
                diff = max(boost, prev_boost) - min(boost, prev_boost)
                step = diff // 2
                prev_boost = boost
                if curr_winner == 'immune_system':
                    # we need to go lower
                    boost -= step
                elif curr_winner == 'infection':
                    boost += step

    def fight_with_immune_system_boost(self, boost):
        return self.fight_with_groups([g.clone_with_boost(boost) if g.is_immune_system() else g.clone() for g in self.groups])

    def fight_with_groups(self, groups):
        while not self.is_concluded(groups):
            targets = self.select_targets(groups)
            attack_order = [(g.army_name, g.id) for g in sorted(groups, key=lambda x: x.initiative, reverse=True)]
            for attacker_identifier in attack_order:
                attacker_army_name, attacker_id = attacker_identifier
                attacker = next((g for g in groups if g.army_name == attacker_army_name and g.id == attacker_id), None)
                target_details = targets[(attacker_army_name, attacker_id)]
                if target_details is not None:
                    groups = [self.attack(attacker, g) if self.is_target(g, target_details) else g.clone()
                              for g in groups]

        for army, remaining in self.remaining_units_by_army(groups).items():
            if remaining > 0:
                return remaining, army
        return 0, None

    def fight(self):
        return self.fight_with_groups([g.clone() for g in self.groups])

    @staticmethod
    def print_health_status(label, groups):
        print(label)
        for g in groups:
            print(g.health_info())

    @staticmethod
    def is_target(group, target_details):
        target_army_name, target_id = target_details
        return group.army_name == target_army_name and group.id == target_id

    @staticmethod
    def attack(attacker, target):
        damage = attacker.potential_damage_to(target)
        units_killed = damage // target.unit_hit_points
        new_total_units = max(target.total_units - units_killed, 0)
        attacked_target = target.clone_with_total_units(new_total_units)
        # print("> ATTACK: %s -> %s (%d units killed with %d damage)" % (attacker.health_info(),
        #                                                              attacked_target.health_info(),
        #                                                              target.total_units - attacked_target.total_units,
        #                                                              damage))
        return attacked_target

    @staticmethod
    def is_concluded(groups):
        return 0 in Battle.remaining_units_by_army(groups).values()

    @staticmethod
    def remaining_units_by_army(groups):
        result = defaultdict(int)
        for group in groups:
            result[group.army_name] += group.total_units
        return dict(result)

    @staticmethod
    def select_targets(groups):
        all_groups = groups
        order = sorted(all_groups, key=lambda x: x.effective_power() * 1000 + x.initiative, reverse=True)

        selections = {}
        remaining_groups = [g for g in all_groups]
        for group in order:
            selection, remaining_groups = group.choose_target(remaining_groups)
            selections[(group.army_name, group.id)] = (selection.army_name, selection.id) if selection else None

        return selections

    def __repr__(self):
        return "Battle"


class Group:
    PATTERN = re.compile(
        r'^(?P<units>\d+) units each with '
        r'(?P<hit_points>\d+) hit points '
        r'(?P<traits>\(.*?\) )?'
        r'with an attack that does '
        r'(?P<attack_power>\d+) '
        r'(?P<attack_type>[a-z]+) damage '
        r'at initiative (?P<initiative>\d+)'
    )

    def __init__(self, army_name, id, total_units, unit_hit_points, immunities, weaknesses, attack_power, attack_type,
                 initiative):
        self.army_name = army_name
        self.id = id
        self.total_units = total_units
        self.unit_hit_points = unit_hit_points
        self.immunities = immunities
        self.weaknesses = weaknesses
        self.attack_power = attack_power
        self.attack_type = attack_type
        self.initiative = initiative

    @staticmethod
    def parse(line, army_name, id):
        r = Group.PATTERN.match(line)
        total_units = int(r.group('units'))
        hit_points = int(r.group('hit_points'))
        attack_power = int(r.group('attack_power'))
        attack_type = r.group('attack_type')
        initiative = int(r.group('initiative'))
        immunities, weaknesses = Group.parse_traits(r.group('traits'))
        return Group(army_name, id, total_units, hit_points, immunities, weaknesses, attack_power, attack_type,
                     initiative)

    @staticmethod
    def parse_traits(segment):
        if segment is None:
            return [], []
        content = segment.strip()[1:-1]
        immunities = []
        weaknesses = []
        for text in content.split(';'):
            if text.strip().startswith('immune to'):
                immunities = [immunity.strip() for immunity in text.strip()[9:].split(',')]
            if text.strip().startswith('weak to'):
                weaknesses = [weakness.strip() for weakness in text.strip()[7:].split(',')]

        return immunities, weaknesses

    def effective_power(self):
        return self.total_units * self.attack_power

    def is_friendly(self, other):
        return other.army_name == self.army_name

    def is_enemy(self, other):
        return not self.is_friendly(other)

    def is_immune_system(self):
        return self.army_name == 'immune_system'

    def global_identifier(self):
        return "%s/%d" % (self.army_name, self.id)

    def potential_damage_to(self, other):
        if self.attack_type in other.immunities:
            return 0
        multiplier = 1
        if self.attack_type in other.weaknesses:
            multiplier = 2

        return self.effective_power() * multiplier

    # During the target selection phase, each group attempts to choose one target. In decreasing order of effective
    # power, groups choose their targets; in a tie, the group with the higher initiative chooses first. The attacking
    # group chooses to target the group in the enemy army to which it would deal the most damage (after accounting for
    # weaknesses and immunities, but not accounting for whether the defending group has enough units to actually receive
    # all of that damage).
    #
    # If an attacking group is considering two defending groups to which it would deal equal damage, it chooses to
    # target the defending group with the largest effective power; if there is still a tie, it chooses the defending
    # group with the highest initiative. If it cannot deal any defending groups damage, it does not choose a target.
    # Defending groups can only be chosen as a target by one attacking group.
    def choose_target(self, other_groups):
        # return the chosen target, or None if no suitable target available, as well as the remaining targets
        best_target = None
        for other_group in other_groups:
            if self == other_group or self.is_friendly(other_group):
                # can't attack self or a friendly target
                continue

            # if the target is dead, don't choose it
            if other_group.total_units == 0:
                continue

            potential_damage_to_enemy = self.potential_damage_to(other_group)

            # if we can't do any damage, we don't choose this target
            if potential_damage_to_enemy == 0:
                continue

            if best_target is None:
                best_target = other_group
            elif potential_damage_to_enemy > self.potential_damage_to(best_target):
                best_target = other_group
            elif potential_damage_to_enemy == self.potential_damage_to(best_target):
                if other_group.effective_power() > best_target.effective_power():
                    best_target = other_group
                elif other_group.effective_power() == best_target.effective_power():
                    if other_group.initiative > best_target.initiative:
                        best_target = other_group

        #  If no best target was chosen, return None and all the enemies as remaining targets. If a best target was
        #  chosen, return it and the enemies with that target removed
        if best_target is None:
            return None, other_groups

        return best_target, [e for e in other_groups if e != best_target]

    def best_target(self, a, b):
        potential_damage_to_a = self.potential_damage_to(a)
        potential_damage_to_b = self.potential_damage_to(b)
        if potential_damage_to_a > potential_damage_to_b:
            return a
        elif potential_damage_to_a < potential_damage_to_b:
            return b

        if a.effective_power() > b.effective_power():
            return a
        elif a.effective_power() < b.effective_power():
            return b

        if a.initiative > b.initiative:
            return a
        else:
            return b

    def summary(self):
        return "%s (#%d)" % (self.army_name, self.id)

    def health_info(self):
        return self.summary() + ", remaining units: %d (%d HP/unit)" % (self.total_units, self.unit_hit_points)

    def clone(self):
        return Group(self.army_name, self.id, self.total_units, self.unit_hit_points, self.immunities, self.weaknesses,
                     self.attack_power, self.attack_type, self.initiative)

    def clone_with_total_units(self, total_units):
        cloned = self.clone()
        cloned.total_units = total_units
        return cloned

    def clone_with_boost(self, boost):
        cloned = self.clone()
        cloned.attack_power += boost
        return cloned

    def __repr__(self):
        return "%s (#%d): %d x HP=%d, AP=%d (%s), EP=%d, %d init (imm: %s, wkn: %s)" % (self.army_name,
                                                                                        self.id,
                                                                                        self.total_units,
                                                                                        self.unit_hit_points,
                                                                                        self.attack_power,
                                                                                        self.attack_type,
                                                                                        self.effective_power(),
                                                                                        self.initiative,
                                                                                        self.immunities,
                                                                                        self.weaknesses)
