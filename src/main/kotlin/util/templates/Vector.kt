package util.templates

import kotlin.math.sqrt

class Vector2D(val x: Int, val y: Int) {
    companion object {
        val NORTH = Vector2D(0, 1)
        val SOUTH = Vector2D(0, -1)
        val EAST = Vector2D(1, 0)
        val WEST = Vector2D(-1, 0)
        val cardinalDirections = setOf(NORTH, SOUTH, EAST, WEST)
    }

    operator fun plus(v: Vector2D): Vector2D {
        return Vector2D(x + v.x, y + v.y)
    }

    operator fun minus(v: Vector2D): Vector2D {
        return this + (v * -1)
    }

    operator fun times(i: Int): Vector2D {
        return Vector2D(x * i, y * i)
    }

    fun copy(): Vector2D {
        return Vector2D(x, y)
    }

    override fun toString(): String {
        return "($x, $y)"
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Vector2D) return false
        return x == other.x && y == other.y
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }
}
