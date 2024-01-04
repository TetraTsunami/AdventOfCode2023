package adventOfCode2023
import util.*
import util.helper.Grid2D
import util.templates.Vector2D

@Suppress("unused")
class Day14(input: String, context: RunContext = RunContext.PROD) : Day(input, context) {
    override fun solve() {
        val grid = Grid2D.fromLines(input)
        var s1 = 0
        plt(grid.toStringLabelled(true, true))
        grid.vectorsOf('O').forEach {
            // starting from the top, move down until our vector
            // count the number of O on the way, and reset count to the height of a #
            val (x, y) = it
            var count = 0
            for (y2 in 0 until y) {
                when (grid[x, y2]) {
                    '.' -> {}
                    '#' -> count = y2 + 1
                    else -> count++
                }
            }
            // grid[x, y] = '.' // for testing
            // grid[x, count] = 'O'
            s1 += grid.height - count
        }
        a(s1) // 107053
        val dirs = listOf(Vector2D.NORTH, Vector2D.WEST, Vector2D.SOUTH, Vector2D.EAST)
        var vectors = grid.vectorsOf('O')
        val pastGrids = mutableListOf(grid.copy())
        for (i in 1..1_000_000_000) {
        // for (i in 1..1) {
            // spin cycle :)
            for (dir in dirs) {
                vectors = shift(grid, dir, vectors)
            }
            if (grid in pastGrids) {
                break
            }
            pastGrids.add(grid.copy())
        }
        // we're assuming that we broke because there's no way we hit a million
        val index = pastGrids.indexOf(grid)
        if (index != -1) {
            val iterationsSoFar = pastGrids.size
            val iterationsLeft = 1_000_000_000 - iterationsSoFar
            val cycle = pastGrids.subList(index, pastGrids.size)
            a(cycle[iterationsLeft % cycle.size].vectorsOf('O')
                .sumOf { grid.height - it.y  }) // 88371

        } else a(grid.vectorsOf('O').sumOf { grid.height - it.y  })
    }

    /**
     * Shifts all 'O' in a direction
     */
    fun shift(grid: Grid2D<Char>, direction: Vector2D,
              vectorsList: List<Vector2D> = grid.vectorsOf('O')): List<Vector2D> {
        var vectors = vectorsList
        val newVectors = mutableListOf<Vector2D>()
        when (direction) {
            Vector2D.EAST, Vector2D.SOUTH -> {
                vectors = vectorsList.reversed()
            }
        }
        vectors.forEach {
            // starting from the top, move down until our vector
            // count the number of O on the way, and reset count to the height of a #
            val (x, y) = it
            var count = 0
            when (direction) {
                Vector2D.NORTH -> {
                    for (y2 in 0 until y) {
                        when (grid[x, y2]) {
                            'O' -> count++
                            '#' -> count = y2 + 1
                        }
                    }
                }
                Vector2D.WEST -> {
                    for (x2 in 0 until x) {
                        when (grid[x2, y]) {
                            'O' -> count++
                            '#' -> count = x2 + 1
                        }
                    }
                }
                Vector2D.SOUTH -> {
                    count = grid.width - 1
                    for (y2 in grid.height - 1 downTo y + 1) {
                        when (grid[x, y2]) {
                            'O' -> count--
                            '#' -> count = y2 - 1
                        }
                    }
                }
                Vector2D.EAST -> {
                    count = grid.width - 1
                    for (x2 in grid.width - 1 downTo x + 1) {
                        when (grid[x2, y]) {
                            'O' -> count--
                            '#' -> count = x2 - 1
                        }
                    }
                }
            }
            when (direction) {
                Vector2D.NORTH, Vector2D.SOUTH -> {
                    grid[x, y] = '.'
                    grid[x, count] = 'O'
                    newVectors.add(Vector2D(x, count))
                }
                Vector2D.EAST, Vector2D.WEST -> {
                    grid[x, y] = '.'
                    grid[count, y] = 'O'
                    newVectors.add(Vector2D(count, y))
                }
            }
        }
        newVectors.sortBy { it.y * 10 + it.x}
        return newVectors
    }
}
