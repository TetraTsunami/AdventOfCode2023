package adventOfCode2023
import util.*
import util.helper.Grid2D
import util.templates.Vector2D

@Suppress("unused")
class Day16(input: String, context: RunContext = RunContext.PROD) : Day(input, context) {
    data class Beam(var position: Vector2D, var direction: Vector2D) {
        val pastTiles = mutableSetOf<Pair<Vector2D, Vector2D>>()
        fun addToPast() {
            pastTiles.add(Pair(position, direction))
        }
        override fun toString(): String {
            return "$position going $direction"
        }
    }
    val vertical = setOf(Vector2D.NORTH, Vector2D.SOUTH)
    val horizontal = setOf(Vector2D.EAST, Vector2D.WEST)
    override fun solve() {
        val grid = Grid2D.fromLines(input)
        a(countEnergized(grid, Vector2D(0,0), Vector2D.EAST)) // 6855
        // part 2
        val candidates = sortedSetOf<Int>()
        for (i in 0 until grid.width) {
            val top = grid.height - 1
            candidates.add(
                countEnergized(grid, Vector2D(i, 0), Vector2D.NORTH))
            candidates.add(
                countEnergized(grid, Vector2D(i, top), Vector2D.SOUTH))
        }
        for (i in 0 until grid.height) {
            val right = grid.width - 1
            candidates.add(
                countEnergized(grid, Vector2D(0, i), Vector2D.EAST))
            candidates.add(
                countEnergized(grid, Vector2D(right, i), Vector2D.WEST))
        }
        a(candidates.max()) // 7513
    }

    fun countEnergized(grid: Grid2D<Char>, startPos: Vector2D, startDir: Vector2D): Int {
        var beams = listOf(Beam(startPos, startDir))
        val completeBeams = mutableListOf<Beam>()
        while (beams.isNotEmpty()) {
            // update each beam's direction, use that to update its position or remove it
            // keep track of past tiles inside each beam
            val beamsNextTick = mutableListOf<Beam>()
            for (beam in beams) {
                // deduplicate beams
                val dedup = Pair(beam.position, beam.direction)
                if (completeBeams.any { dedup in it.pastTiles } ||
                    beams.any { dedup in it.pastTiles }) {
                    completeBeams.add(beam)
                    continue
                }
                // retrieve current tile
                if (!grid.isInBounds(beam.position)) {
                    completeBeams.add(beam)
                    continue
                }
                beam.addToPast()
                val curTile = grid[beam.position]
                // resolve current tile and find next position
                when (curTile) {
                    '.' -> {}
                    '\\' -> {
                        beam.direction = Vector2D(beam.direction.y, beam.direction.x)
                    }
                    '/' -> {
                        beam.direction = Vector2D(-beam.direction.y, -beam.direction.x)
                    }
                    '|' -> {
                        if (beam.direction in horizontal) {
                            beam.direction = Vector2D.SOUTH
                            beamsNextTick.add(Beam(beam.position + Vector2D.NORTH, Vector2D.NORTH))
                        }
                    }
                    '-' -> {
                        if (beam.direction in vertical) {
                            beam.direction = Vector2D.EAST
                            beamsNextTick.add(Beam(beam.position + Vector2D.WEST, Vector2D.WEST))
                        }
                    }
                }
                beam.position += beam.direction
                beamsNextTick.add(beam)
            }
            beams = beamsNextTick
        }
        val energized = completeBeams.flatMap { it.pastTiles.map { it.first } }.toSet()
        // make a pretty map :)
        if (context == RunContext.TEST) {
            plt()
            val copy = grid.copy()
            energized.forEach {
                copy[it] = '#'
            }
            plt(copy)
        }
        return energized.size
    }
}
