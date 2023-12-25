package adventOfCode2023
import util.*

@Suppress("unused")
class Day12(input: String, context: RunContext = RunContext.PROD) : Day(input, context) {
    val variationsMemo = mutableMapOf<Triple<List<Char>, List<Int>, Boolean>, Long>()
    override fun solve() {
        var s1 = 0L
        for (line in lines) {
            val (springs, inter) = line.split(" ")
            val nums = inter.split(",").map { it.toInt() }
            val vars = numVariations(springs.toList(), nums, false)
            s1 += vars
        }
        a(s1)

        var s2 = 0L
        for (line in lines) {
            var (springs, inter) = line.split(" ")
            springs = (0..4).map { springs }.joinToString("?")
            val inter2 = inter.split(",").map { it.toInt() }
            val nums = (0..4).map { inter2 }.flatten()
            val vars = numVariations(springs.toList(), nums, false)
            s2 += vars

        }
        a(s2)
    }

    fun numVariations(s: List<Char>, badSprings: List<Int>, decreasing: Boolean): Long {
        // base case: no bad springs, no more string
        if (s.isEmpty()) {
            return if (badSprings.isEmpty() || (badSprings[0] == 0 && badSprings.size == 1)) 1L else 0L
        }
        if (badSprings.isEmpty()) {
            return if (s.none { it == '#' }) 1L else 0L
        }
        if (badSprings[0] < 0) return 0
        // ???.### 1,1,3
        // -> .??.### 1,1,3
        // -> #??.### 0,1,3
        val firstChar = s.first()
        return variationsMemo.getOrPut(Triple(s, badSprings, decreasing)) {
            when (firstChar) {
                '#' -> hash(s, badSprings)
                '.' -> dot(s, badSprings, decreasing)
                '?' -> hash(s, badSprings) + dot(s, badSprings, decreasing)
                else -> 0
            }
        }
    }

    fun hash(s: List<Char>, b: List<Int>): Long {
        // remove first char
        // decrement first bad spring
        return numVariations(s.drop(1),
            b.toMutableList().let { it[0]-- ; it }, true)
    }
    fun dot(s: List<Char>, b: List<Int>, decreasing: Boolean): Long {
        // remove first char, respect spacing
        if (b[0] == 0) {
            return numVariations(s.drop(1), b.drop(1), false)
        }
        if (decreasing) return 0
        return numVariations(s.drop(1), b, false)
    }
}
