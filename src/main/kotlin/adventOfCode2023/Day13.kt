package adventOfCode2023
import util.*
import util.helper.Grid2D
import util.helper.Itertools
import java.util.Collections

@Suppress("unused")
class Day13(input: String, context: RunContext = RunContext.PROD) : Day(input, context) {
    override fun solve() {
        val grids = input.split("\n\n").filter { it.isNotEmpty() }
        var s1 = 0
        for (gridPacked in grids) {
            val grid = Grid2D.fromLines(gridPacked)
            val mirrorColumn = findMirrorIndexLines(grid.columns())
            val mirrorRow = findMirrorIndexLines(grid.rows())
            if (mirrorColumn != -1) {
                prettyMirror(grid, mirrorColumn, true)
                s1 += mirrorColumn + 1
            }
            if (mirrorRow != -1) {
                prettyMirror(grid, mirrorRow)
                s1 += (mirrorRow + 1) * 100
            }
        }
        a(s1) // 34202
        var s2 = 0
        for (gridPacked in grids) {
            val grid = Grid2D.fromLines(gridPacked)
            val mirrorColumn = findMirrorIndexSmudge(grid.columns())
            val mirrorRow = findMirrorIndexSmudge(grid.rows())
            if (mirrorColumn != -1) {
                prettyMirror(grid, mirrorColumn, true)
                s2 += mirrorColumn + 1
            }
            if (mirrorRow != -1) {
                prettyMirror(grid, mirrorRow)
                s2 += (mirrorRow + 1) * 100
            }
        }
        a(s2) // 34230

    }

    fun prettyMirror(grid: Grid2D<Char>, mirrorIndex: Int, column: Boolean = false) {
        plt()
        when (column) {
            true -> {
                plt(grid)
                plt(" ".repeat(mirrorIndex) + ">< ${mirrorIndex + 1} left")
            }
            false -> {
                val lines = grid.toString().lines().toMutableList()
                lines[mirrorIndex] += " v ${mirrorIndex + 1} up"
                lines[mirrorIndex + 1] += " ^"
                plt(lines.joinToString("\n"))
            }
        }
    }

    fun findMirrorIndexLines(lines: List<List<Char>>): Int {
        val duplicatedLines = lines.map { line -> lines.indexOfFirst { it == line }}
        return findMirrorIndex(duplicatedLines)
    }

    fun findMirrorIndex(lines: List<Int>, ignore: Int = -1): Int {
        // mirror could be any single pair of two duplicated lines in a row
        for (i in 0 until lines.size - 1) {
            if (lines[i] == lines[i + 1]) {
                if (i == ignore) continue
                // potential mirror index
                val l1 = lines.subList(0, i + 1).reversed()
                val l2 = lines.subList(i + 1, lines.size)
                // lists of ascending distance from potential mirror
                if (l1.isEmpty() || l2.isEmpty()) return -1
                // one list will be a sublist of the other if there is a mirror
                Collections.indexOfSubList(l1, l2).let { if (it == 0) return i }
                Collections.indexOfSubList(l2, l1).let { if (it == 0) return i }
            }
        }
        return -1
    }

    fun findMirrorIndexSmudge(lines: List<List<Char>>): Int {
        plt()
        val duplicatedLines = lines.map { line -> lines.indexOfFirst { it == line }}
        val oldMirrorIndex = findMirrorIndex(duplicatedLines)
        val lineTypes = lines.associateBy { line -> lines.indexOfFirst { it == line } }
        // find lines types that
        // 1) already exist in the map
        // 2) are 1 character away from another line type
        val swapCandidates = mutableListOf<Pair<Int, Int>>()
        Itertools.combinations(lineTypes.keys, 2).forEach {
            val (i1, i2) = it
            if (it.distinct().size == 1) return@forEach
            val l1 = lineTypes[i1]!!
            val l2 = lineTypes[i2]!!
            var diff = 0
            for (i in l1.indices) {
                if (l1[i] != l2[i]) diff++
            }
            if (diff == 1) {
                swapCandidates.add(Pair(i1, i2))
            }
        }
        val mirrorIndexCandidates = mutableListOf<Int>()
        // every instance of a swappable line type should be swapped,
        // then tested to see if there's a mirror
        swapCandidates.forEach { pair ->
            val (i1, i2) = pair
            // test for mirror with swapping one instance of i1 in duplicated lines with i2
            // repeat for all instances of i1, then do the same for i2
            duplicatedLines.forEachIndexed {
                i, line ->
                if (line == i1) {
                    findMirrorIndex(duplicatedLines.toMutableList()
                        .apply { this[i] = i2 }, oldMirrorIndex)
                        .let { if (it != -1) {
                            plt("\tSwapped ${lineTypes[i1]?.joinToString("")} for ${lineTypes[i2]?.joinToString("")} at $i, found mirror index: $it")
                            mirrorIndexCandidates.add(it)
                        } }
                } else if (line == i2) {
                    findMirrorIndex(duplicatedLines.toMutableList()
                        .apply { this[i] = i1 }, oldMirrorIndex)
                        .let { if (it != -1) {
                            plt("\tSwapped ${lineTypes[i2]?.joinToString("")} for ${lineTypes[i1]?.joinToString("")} at $i, found mirror index: $it")
                            mirrorIndexCandidates.add(it)
                        } }
                }
            }
        }
        return mirrorIndexCandidates.maxOrNull() ?: -1
    }
}
