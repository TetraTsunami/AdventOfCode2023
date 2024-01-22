package adventOfCode2023
import okio.ByteString.Companion.decodeHex
import util.*
import util.templates.InfiniteGrid2D
import util.templates.Vector2D

@Suppress("unused")
class Day18(input: String, context: RunContext = RunContext.PROD) : Day(input, context) {
    private val dirToVec = mapOf(
        'U' to Vector2D.NORTH,
        'D' to Vector2D.SOUTH,
        'L' to Vector2D.WEST,
        'R' to Vector2D.EAST,
    )
    private val intToVec = mapOf(
        '0' to Vector2D.WEST,
        '1' to Vector2D.SOUTH,
        '2' to Vector2D.EAST,
        '3' to Vector2D.NORTH
    )
    override fun solve() {
        val grid = InfiniteGrid2D(' ')
        var current = Vector2D(0, 0)
        // outline
        for (line in lines) {
            val parts = line.split(" ")
            val dir = parts[0].first()
            val amt = parts[1]
            for (i in 0 until amt.toInt()) {
                current += dirToVec[dir]!!
                grid[current] = '#'
            }
        }
        // fill, like day 10 but without directional cues
        for (y in grid.minY()..grid.maxY()) {
            var N = false
            var S = false
            for (x in grid.minX()..grid.maxX()) {
                val position = Vector2D(x, y)
                if (grid[position] == '#') {
                    if (grid[position + Vector2D.NORTH] == '#') N = !N
                    if (grid[position + Vector2D.SOUTH] == '#') S = !S
                } else if (N && S) {
                    grid[position] = '@'
                }
            }
        }
        a(grid.sumOf { _, c -> if (c != ' ') 1 else 0 })

        // part 2
        val vertices = mutableListOf(Vector2D(0, 0))
        var s2 = 0L
        current = Vector2D(0, 0)
        for (line in lines) {
            val parts = line.split(" ")
            val amt = Integer.parseInt(parts[2].substring(2, 7), 16)
            val dir = intToVec[parts[2][7]]!!
            current += dir * amt
            s2 += amt
            vertices.add(current)
        }
        // plan for "fill":
        // given row, find all vertex pairs that intersect it
        // for each pair, find the x value of the intersection
        // fill from there?
        // next row: if the vertex pairs are the same, copy last row
        val edges = listOf(vertices.minBy { it.x }.x, vertices.maxBy { it.x }.x)
        val intersectionCache = mutableMapOf<List<Int>, Int>()
        for (y in vertices.maxBy { it.y }.y downTo vertices.minBy { it.y }.y) {
            val intersections = mutableListOf<Int>()
            for (i in vertices.indices) {
                val v1 = vertices[i]
                val v2 = vertices[if (i == vertices.size - 1) 0 else i + 1]
                if (v1.y == v2.y) continue
                if (v1.y < y && v2.y < y) continue
                if (v1.y > y && v2.y > y) continue
                intersections.add(i) // note down the index of the first vertex.
            }
            /*
            #--#
            #--#
            #--#### -> seen as #--#  #----... which is pretty bad, actually.
            #-----#    means we need to do more north-south checks.
            #-----#    is this going to cause problems with caching? probably not?
             */
            s2 += intersectionCache.getOrPut(intersections) {
                // get x values of intersections. they're all vertical, so we can just use x
                val xVals = intersections.map { vertices[it].x }.sorted()
                pl(xVals)
                return@getOrPut 0
            }
        }
        a(s2)
    }
}
