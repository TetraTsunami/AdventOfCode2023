package adventOfCode2023
import util.*
import util.templates.Grid2D
import util.templates.Vector2D
import java.util.*

@Suppress("unused")
class Day17(input: String, context: RunContext = RunContext.PROD) : Day(input, context) {
    override fun solve() {
        // we're going to come back to this later
        a(0, 0)
        val grid = Grid2D.fromLines(input).map { it.digitToIntOrNull() ?: 0 }
        val start = Vector2D(0, 0)
        val end = Vector2D(grid.width - 1, grid.height - 1)
        val costSoFar = mapDijkstrasAlgoSearch(grid, start)
        plt(Grid2D.intGridToString(Grid2D.fromVectors(costSoFar, 0).map { it ?: 0 }))
        // plt(Grid2D.fromVectors(fromDir.mapValues { it.value?.toChar() ?: ' ' }, ' '))
        // val path = mutableListOf<Vector2D>()
        // var current = end
        // while (current != start) {
        //     path.add(current)
        //     current += fromDir[current]!!
        // }
        // path.add(start)
        // plt(Grid2D.fromVectors(path.associateWith { ((fromDir[it] ?: Vector2D(0, 0)) * -1).toChar()}, ' '))
        a(costSoFar[end] ?: -1)
        // 1245 too low
        // 1301 too high
    }

    private fun mapDijkstrasAlgoSearch(grid: Grid2D<Int>, start: Vector2D):
            Map<Vector2D, Int> {
        data class Node(
            val coord: Vector2D,
            val direction: Vector2D,
            val steps: Int,
            val cost: Int
        ) {
            val triple = Triple(coord, direction, steps)
        }

        val first = Node(start, Vector2D(0, 0), 0, 0)
        val frontier = PriorityQueue<Node>(compareBy { it.cost })
        frontier.add(first)
        // Memory: coord, direction, consecutive steps -> cost
        val memory = mutableMapOf(first.triple to 0)

        while (frontier.isNotEmpty()) {
            val node = frontier.remove()
            val neighbors = grid.getNeighbors(node.coord).toMutableList()

            for (next in neighbors) {
                val newCost = node.cost + grid[next]
                val diff = node.coord - next
                val isConsec = diff.isParallel(node.direction)
                if (isConsec && node.steps >= 2) {
                    continue
                }
                val nextState = Node(next, diff, if (isConsec) node.steps + 1 else 0, newCost)

                if (newCost < memory.getOrDefault(nextState.triple, Int.MAX_VALUE)) {
                    memory[nextState.triple] = newCost
                    frontier.add(nextState)
                }
            }
        }
        // make map of coord -> lowest cost
        plt(Grid2D.fromVectors(memory.toList().sortedBy { -it.second }.associate { it.first.first to it.first.second.toChar() }, ' '))
        return memory.toList().sortedBy { -it.second }.associate { it.first.first to it.second }
    }
}
