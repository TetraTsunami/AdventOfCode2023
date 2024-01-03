package adventOfCode2023
import util.*

@Suppress("unused")
class Day03(input: String, context: RunContext = RunContext.PROD) : Day(input, context) {
    override fun solve() {
        var s1 = 0
        var s2 = 0
        for ((i, line) in lines.withIndex()) {
            val symbolsAbove = findSymbols(lines[maxOf(i - 1, 0)])
            val symbolsBelow = findSymbols(lines[minOf(i + 1, lines.size - 1)])
            val symbols = findSymbols(line)
            // if number is adjacent to a symbol, add to sum
            var j = 0
            while (j < line.length) {
                val c = line[j]
                if (Character.isDigit(c) && (trueAround(j, symbolsAbove) || trueAround(j, symbolsBelow) || trueAround(j, symbols))) {
                    val (res, upper) = restOfNumber(line, j)
                    s1 += res
                    j = upper
                }
                j++
            }
            val gears = findGears(line)
            for (gear in gears) {
                val adjacent = mutableListOf<Int>()
                var lineNo = i - 1
                while (lineNo <= i + 1) {
                    var charIndex = gear - 1
                    while (charIndex <= gear + 1) {
                        if (charIndex == gear && lineNo == i) {
                            charIndex++
                            continue
                        }
                        if (charIndex < 0 || charIndex >= line.length || lineNo < 0 || lineNo >= lines.size) {
                            charIndex++
                            continue
                        }
                        plt(lineNo, charIndex, line[lineNo], line[charIndex])
                        if (lines[lineNo][charIndex].isDigit()) {
                            val (res, index) = restOfNumber(lines[lineNo], charIndex)
                            adjacent.add(res)
                            charIndex = index
                        }
                        charIndex++
                    }
                    lineNo++

                }
                if (adjacent.size == 2) {
                    s2 += adjacent.reduce { acc, e -> acc * e }
                }
            }
        }
        a(s1, s2)
    }
    fun findSymbols(line: String): BooleanArray {
        return line.map { c -> !c.isDigit() && c != '.' }.toBooleanArray()
    }
    fun findGears(line: String): List<Int> {
        return line.mapIndexed { i, c -> if (c == '*') i else -1 }.filter { it != -1 }
    }
    fun trueAround(index: Int, bools: BooleanArray): Boolean {
        return bools[maxOf(index - 1, 0)] || bools[index] || bools[minOf(index + 1, bools.size - 1)]
    }
    fun restOfNumber(line: String, index: Int): Pair<Int, Int> {
        var lower = index
        var upper = index
        for (i in index downTo 0) {
            if (!Character.isDigit(line[i])) {
                break
            }
            lower = i
        }
        for (i in index until line.length) {
            if (!Character.isDigit(line[i])) {
                break
            }
            upper = i
        }
        return Pair(line.substring(lower, upper + 1).toInt(), upper + 1)
    }
}
