class Recipes:
    def __init__(self, recipes: list, a: int, b: int):
        self.recipes = recipes
        self.a = a
        self.b = b

    def tick(self):
        score = self.recipes[self.a] + self.recipes[self.b]
        self.recipes += [int(c) for c in list(str(score))]
        self.a = (self.a + self.recipes[self.a] + 1) % len(self.recipes)
        self.b = (self.b + self.recipes[self.b] + 1) % len(self.recipes)

    def get_following_10_scores_after(self, number):
        number = int(number)
        while len(self.recipes) < number + 10:
            self.tick()

        return ''.join([str(r) for r in self.recipes[number:number + 10]])

    def get_total_recipes_before(self, number):
        target = [int(c) for c in list(str(number))]
        target_length = len(target)

        while True:
            if self.recipes[-target_length:] == target:
                return len(self.recipes) - target_length

            if self.recipes[-target_length-1:-1] == target:
                return len(self.recipes) - target_length - 1

            self.tick()
