package adventOfCode2023
import util.*
import util.helper.Grid2D
import util.templates.Vector2D

@Suppress("unused")
class Day10(input: String, context: RunContext = RunContext.PROD) : Day(input, context) {
    val pipes = mapOf(
        '|' to setOf(Vector2D.NORTH, Vector2D.SOUTH),
        '-' to setOf(Vector2D.EAST, Vector2D.WEST),
        'L' to setOf(Vector2D.SOUTH, Vector2D.EAST),
        '7' to setOf(Vector2D.NORTH, Vector2D.WEST),
        'J' to setOf(Vector2D.SOUTH, Vector2D.WEST),
        'F' to setOf(Vector2D.NORTH, Vector2D.EAST),
        '.' to setOf(),
        'S' to setOf(),
    )

    override fun solve() {
        // find animal starting position
        val grid = Grid2D.fromLines(lines.map { it.toList() }, '.')
        val animal = grid.vectorOf('S')!!
        val vd1 = mutableListOf<Vector2D>()
        var cur = animal.copy()
        var nextDir: Vector2D?
        plt(grid)
        plt("Animal at $cur")
        plt()
        grid.getNeighbors(cur)
            .first { nextDir(cur, it.second, it.first) != null }
            .let {
                nextDir = nextDir(cur, it.second, it.first)
                plt("$nextDir, ${it.first}")
                cur = it.second
            }
        plt("Starting at $cur, going $nextDir")

        while (cur != animal) {
            grid[cur + nextDir!!]
                .let { pipe ->
                    vd1.add(cur)
                    val validDirs = pipes[pipe]!!
                    cur += nextDir!!
                    nextDir = validDirs.firstOrNull { it != (nextDir!! * -1) }
                    plt("Pos: $cur, NextDir: $nextDir, pipe: $pipe, validDirs: $validDirs")
                }
        }
        a(vd1.size / 2 + 1)

        // draw path out of vd1 (it's pretty, and maybe also helps for part 2)
        val newGrid = Grid2D(lines[0].length, lines.size, ' ')
        newGrid[animal.x, animal.y] = 'S'
        for (v in vd1) {
            newGrid[v.x, v.y] = grid[v]
        }
        var s2 = 0
        for (y in 0 until newGrid.height) {
            var p = 0
            var lastSeen = ' '
            for (x in 0 until newGrid.width) {
                when (newGrid[x, y]) {
                    ' ' -> {
                        if (p % 2 == 1) {
                            s2++
                            newGrid[x, y] = 'I'
                        }
                        lastSeen = ' '
                    }
                    '|' -> {
                        lastSeen = ' '
                        p++
                    }
                    'F', '7' -> {
                        if (lastSeen == 'J') {
                            p++
                        } else {
                            lastSeen = 'F'
                        }
                    }
                    'J', 'L' -> {
                        if (lastSeen == 'F') {
                            p++
                        } else {
                            lastSeen = 'J'
                        }
                    }
                }
            }
        }
        pl(newGrid)
        a(s2)

    }

    fun nextDir(start: Vector2D, target: Vector2D, targetValue: Char): Vector2D? {
        val diff = target - start
        // target value should be in pipes, see if our diff is valid and return the other one
        val validDirs = pipes[targetValue]!!
        if ((diff * -1) in validDirs) {
            return validDirs.first { it != (diff * -1) }
        }
        return null
    }
}
