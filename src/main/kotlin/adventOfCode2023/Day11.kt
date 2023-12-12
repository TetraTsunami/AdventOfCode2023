package adventOfCode2023
import util.*
import util.helper.Grid2D
import util.templates.Vector2D
import util.helper.Itertools.combinations as combinations

@Suppress("unused")
class Day11(input: String, context: RunContext = RunContext.PROD) : Day(input, context) {
    override fun solve() {
        val grid = Grid2D.fromLines(lines.map { it.toList() }, '.')
        // expand lines, columns with only default value
        val doubleRows = grid.rows().mapIndexed {i, it -> if (it.all { it == '.' }) i else -1 }
            .filter { it != -1 }.toSet()
        val doubleCols = grid.columns().mapIndexed {i, it -> if (it.all { it == '.' }) i else -1 }
            .filter { it != -1 }.toSet()

        val galaxies = grid.vectorsOf('#').map { g ->
            g + Vector2D(doubleCols.count { it < g.x },
                doubleRows.count { it < g.y })}
        a(combinations(galaxies, 2).sumOf { (g1, g2) -> g1.manhattanDistance(g2).toLong() })
        val galaxies2 = grid.vectorsOf('#').map { g ->
            g + Vector2D(doubleCols.count { it < g.x } * (1000000 - 1),
                doubleRows.count { it < g.y } * (1000000 - 1))}
        a(combinations(galaxies2, 2).sumOf { (g1, g2) -> g1.manhattanDistance(g2).toLong() })
    }
}
