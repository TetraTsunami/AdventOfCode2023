package util.templates

/**
 * A 2D grid of values. The origin is (0, 0) in the top-left corner, and the y-axis is inverted.
 * @param width The width of the grid.
 * @param height The height of the grid.
 * @param default The default value of the grid.
 */
class Grid2D<T>(val width: Int, val height: Int, val default: T) {
    private val grid = MutableList(height) { MutableList(width) { default } }

    /**
     * Returns the value at the given vector. The origin is (0, 0) in the bottom-left corner.
     */
    operator fun get(x: Int, y: Int): T {
        return grid[y][x]
    }

    operator fun get(vector: Vector2D): T {
        return this[vector.x, vector.y]
    }

    operator fun set(x: Int, y: Int, value: T) {
        grid[y][x] = value
    }

    operator fun set(vector: Vector2D, value: T) {
        this[vector.x, vector.y] = value
    }

    fun rows(): List<List<T>> {
        return grid.map { it.toList() }
    }
    fun columns(): List<List<T>> {
        val columns = mutableListOf<MutableList<T>>()
        for (x in 0 until width) {
            columns.add(MutableList(height) { this[x, it] })
        }
        return columns.map { it.toList() }
    }

    /**
     * Returns the neighbors of the given vector (E, W, N, S, optionally NE, NW, SE, SW).
     * @param includeDiagonals Whether to include diagonal neighbors.
     * @param includeSelf Whether to include the vector itself.
     * @return A list of neighbor vectors from the origin.
     */
    fun getNeighbors(x: Int, y: Int, includeDiagonals: Boolean = false, includeSelf: Boolean = false): Iterable<Vector2D> {
        val neighbors = mutableListOf<Vector2D>()
        neighbors.addAll(arrayOf(
            Vector2D(x + 1, y),
            Vector2D(x - 1, y),
            Vector2D(x, y + 1),
            Vector2D(x, y - 1),
        ))
        if (includeDiagonals) {
            neighbors.addAll(arrayOf(
                Vector2D(x + 1, y + 1),
                Vector2D(x - 1, y + 1),
                Vector2D(x + 1, y - 1),
                Vector2D(x - 1, y - 1),
            ))
        }
        if (includeSelf) neighbors.add(Vector2D(x, y))
        return neighbors.filter { isInBounds(it)}
    }

    /**
     * Returns the neighbors of the given vector (E, W, N, S, optionally NE, NW, SE, SW).
     * @param includeDiagonals Whether to include diagonal neighbors.
     * @param includeSelf Whether to include the vector itself.
     * @return A list of neighbor vectors from the origin.
     */
    fun getNeighbors(vector: Vector2D, includeDiagonals: Boolean = false, includeSelf: Boolean = false): Iterable<Vector2D> {
        return getNeighbors(vector.x, vector.y, includeDiagonals, includeSelf)
    }

    /**
     * Get neighbors for pathfinding (E, W, N, S but sometimes reversed)
     * This helps make pretty zigzag paths rather than boring straight lines, see
     * https://www.redblobgames.com/pathfinding/a-star/implementation.html#ties-checkerboard-neighbors
     * @param vector The vector to get neighbors for.
     * @return A list of neighbor vectors from the origin.
     */
    fun getAltNeighbors(vector: Vector2D): Iterable<Vector2D> {
        val neighbors = getNeighbors(vector, false, false)
        if ((vector.x + vector.y) % 2 == 1) {
            return neighbors
        }
        return neighbors.reversed()
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
        for (line in grid) {
            for ((x, el) in line.withIndex()) {
                if (el == value) vectors.add(Vector2D(x, grid.indexOf(line)))
            }
        }
        return vectors
    }

    override fun toString(): String {
        return grid.joinToString("\n") { it.joinToString("") }
    }

    fun toStringLabelled(): String {
        val string = this.toString()
        var lines = string.lines().toMutableList()
        lines = lines.mapIndexed { i, it -> "$it $i" }.toMutableList()
        lines.add(" ".repeat(width).mapIndexed { i, _ -> i % 10 }.joinToString(""))
        return lines.joinToString("\n")
    }

    fun toLines(): List<List<T>> {
        return grid.map { it.toList() }
    }

    fun copy(): Grid2D<T> {
        return fromLines(grid, default)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Grid2D<*>) return false
        if (other.width != width || other.height != height) return false
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (this[x, y] != other[x, y]) return false
            }
        }
        return true
    }

    fun isInBounds(coordinate: Vector2D): Boolean {
        if (coordinate.x > width - 1 || coordinate.x < 0) return false
        if (coordinate.y > height - 1 || coordinate.y < 0) return false
        return true
    }

    fun <U> map(transform: (T) -> U): Grid2D<U> {
        val newGrid = Grid2D(width, height, transform(default))
        for (y in 0 until height) {
            for (x in 0 until width) {
                newGrid[x, y] = transform(this[x, y])
            }
        }
        return newGrid
    }

    companion object {
        fun <T> fromLines(lines: List<List<T>>, default: T): Grid2D<T> {
            val grid = Grid2D(lines[0].size, lines.size, default)
            for ((y, line) in lines.withIndex()) {
                for ((x, el) in line.withIndex()) {
                    grid[x, y] = el
                }
            }
            return grid
        }
        fun fromLines(lines: String, default: Char = ' '): Grid2D<Char> {
            val split = lines.lines().filter { it.isNotBlank() }
            val grid = Grid2D(split[0].length, split.size, default)
            for ((y, line) in split.withIndex()) {
                for ((x, el) in line.withIndex()) {
                    grid[x, y] = el
                }
            }
            return grid
        }
        fun <T> fromVectors(vectors: Map<Vector2D, T?>, default: T?): Grid2D<T?> {
            val width = vectors.maxBy { it.key.x }.key.x + 1
            val height = vectors.maxBy { it.key.y }.key.y + 1
            val grid = Grid2D(width, height, default)
            for ((vector, value) in vectors) {
                grid[vector] = value
            }
            return grid
        }

        fun intGridToString(grid: Grid2D<Int>): String {
            // the idea is to center numbers so the grid is readable
            val max = grid.grid.flatten().max()
            val maxLen = max.toString().length
            val stringGrid = grid.map { it.toString().padStart(maxLen, ' ') + " " }
            return stringGrid.toString()
        }
    }
}
