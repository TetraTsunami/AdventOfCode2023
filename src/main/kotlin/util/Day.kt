// Heavily inspired by SizableShrimp's structure :)
package util

class DayCompleteException(message: String) : Exception(message)
abstract class Day(protected var input: String) {
    protected val startTime = System.currentTimeMillis()
    protected val lines: List<String>
    protected var part1Solved = false
    protected var part2Solved = false

    init {
        input = parseInput()
        lines = parseToList()
    }

    @Suppress("unused")
            /**
             * Run the day's puzzle, parsing input if neccessary and printing the result.
             */
    fun run() {
        try {
            solve()
        } catch (_: DayCompleteException) {}
    }

    /**
     * Solve the day's puzzle.
     */
    abstract fun solve()

    /**
     * Used in constructor to parse input. Override this if it's helpful to parse differently.
     */
    fun parseToList(): List<String> {
        val lines = input.lines()
        if (lines.last().isEmpty()) return lines.dropLast(1)
        return lines
    }

    /**
     * Parse input to a string.
     */
    fun parseInput(): String {
        if (input.lines().size == 2) {
            // single line
            return input.dropLast(2) // drop \r\n
        }
        return input
    }

    /**
     * Represents solving a part of the day's puzzle. Prints immediately to the console.
     */
    fun a(inp: Any) {
        val endTime = System.currentTimeMillis()
        if (!part1Solved) {
            println("\tPart 1: $inp (${(endTime - startTime) / 1000.0}s)")
            part1Solved = true
            return
        }
        if (!part2Solved) {
            println("\tPart 2: $inp (${(endTime - startTime) / 1000.0}s)")
            part2Solved = true
        }
        throw DayCompleteException("Both parts solved")
    }

    /**
     * Represents solving both parts of the day's puzzle. Prints immediately to the console.
     */
    fun a(vararg inp: Any) {
        for (i in inp) {
            a(i)
        }
    }
}
