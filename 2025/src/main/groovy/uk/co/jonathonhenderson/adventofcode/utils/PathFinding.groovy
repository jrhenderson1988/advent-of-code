package uk.co.jonathonhenderson.adventofcode.utils

class PathFinding {
    static <T> List<T> bfs(T source, Closure<Boolean> target, Closure<List<T>> neighbourFn) {
        def q = [source]
        def explored = [source].toSet()
        def prev = [(source): null]

        while (!q.isEmpty()) {
            def v = q.pop()
            if (target.call(v) == true) {
                def path = []
                while (v != null) {
                    path.push(v)
                    v = prev[v]
                }
                return path
            }
            for (def w in neighbourFn.call(v)) {
                if (!explored.contains(w)) {
                    explored.add(w)
                    prev[w] = v
                    q.add(w)
                }
            }
        }

        return null
    }
}
