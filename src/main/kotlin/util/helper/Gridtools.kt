package util.helper

import util.templates.Vector2D

/**
 * A 2D grid of values. The origin is (0, 0) in the bottom-left corner!!!
 * @param width The width of the grid.
 * @param height The height of the grid.
 * @param default The default value of the grid.
 */
class Grid2D<T>(val width: Int, val height: Int, val default: T) {
    private val grid = MutableList(width) { MutableList(height) { default } }

    /**
     * Returns the value at the given vector. The origin is (0, 0) in the bottom-left corner.
     */
    operator fun get(x: Int, y: Int): T {
        return grid[grid.size - 1 - y][x]
    }

    operator fun get(vector: Vector2D): T {
        return this[vector.x, vector.y]
    }

    operator fun set(x: Int, y: Int, value: T) {
        grid[grid.size - 1 - y][x] = value
    }

    operator fun set(vector: Vector2D, value: T) {
        this[vector.x, vector.y] = value
    }

    /**
     * Returns the neighbors of the given vector (sorted by lowest y, then lowest x).
     * @param includeDiagonals Whether to include diagonal neighbors.
     * @param includeSelf Whether to include the vector itself.
     * @return A list of pairs of the neighbor value and the neighbor vector from the origin.
     */
    fun getNeighbors(x: Int, y: Int, includeDiagonals: Boolean = false, includeSelf: Boolean = false): List<Pair<T, Vector2D>> {
        val neighbors = mutableListOf<Pair<T, Vector2D>>()
        val test = this[x, y]
        if (x > 0) neighbors.add(Pair(this[x - 1, y], Vector2D(x - 1, y)))
        if (x < width - 1) neighbors.add(Pair(this[x + 1, y], Vector2D(x + 1, y)))
        if (y > 0) neighbors.add(Pair(this[x, y - 1], Vector2D(x, y - 1)))
        if (y < height - 1) neighbors.add(Pair(this[x, y + 1], Vector2D(x, y + 1)))
        if (includeDiagonals) {
            if (x > 0 && y > 0) neighbors.add(Pair(this[x - 1, y - 1], Vector2D(x - 1, y - 1)))
            if (x < width - 1 && y > 0) neighbors.add(Pair(this[x + 1, y - 1], Vector2D(x + 1, y - 1)))
            if (x > 0 && y < height - 1) neighbors.add(Pair(this[x - 1, y + 1], Vector2D(x - 1, y + 1)))
            if (x < width - 1 && y < height - 1) neighbors.add(Pair(this[x + 1, y + 1], Vector2D(x + 1, y + 1)))
        }
        if (includeSelf) neighbors.add(Pair(grid[x][y], Vector2D(x, y)))
        neighbors.sortBy { it.second.x }
        neighbors.sortBy { it.second.y }
        return neighbors
    }

    fun getNeighbors(vector: Vector2D, includeDiagonals: Boolean = false, includeSelf: Boolean = false): List<Pair<T, Vector2D>> {
        return getNeighbors(vector.x, vector.y, includeDiagonals)
    }

    /**
     * Returns the first vector that matches the given value (sorted by y, then x), or null if none are found.
     */
    fun vectorOf(value: T): Vector2D? {
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (this[x, y] == value) return Vector2D(x, y)
            }
        }
        return null
    }

    /**
     * Returns all vectors with the given value (sorted by y, then x).
     */
    fun vectorsOf(value: T): List<Vector2D> {
        val vectors = mutableListOf<Vector2D>()
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (this[x, y] == value) vectors.add(Vector2D(x, y))
            }
        }
        return vectors
    }

    override fun toString(): String {
        return grid.joinToString("\n") { it.joinToString("") }
    }

    fun toLines(): List<List<T>> {
        return grid.map { it.toList() }
    }

    companion object {
        fun <T> fromLines(lines: List<List<T>>, default: T): Grid2D<T> {
            val grid = Grid2D(lines[0].size, lines.size, default)
            for ((y, line) in lines.withIndex()) {
                for ((x, el) in line.withIndex()) {
                    grid[x, lines.size - 1 - y] = el
                }
            }
            return grid
        }
    }
}
