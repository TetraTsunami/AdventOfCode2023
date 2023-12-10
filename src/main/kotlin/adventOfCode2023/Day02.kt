package adventOfCode2023
import util.*

@Suppress("unused")
class Day02(input: String, context: RunContext = RunContext.PROD) : Day(input, context) {
    override fun solve() {
        var sum1 = 0
        var sum2 = 0
        for (line in lines) {
        //     game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            val parts = line.split(" ")
            val id = parts[1].replace(":", "")
            val rest = line.substringAfter(": ")
            val sets = rest.split("; ")
            var maxR = 0
            var maxG = 0
            var maxB = 0
            for (set in sets) {
                val colors = set.split(", ")
                for (color in colors) {
                    val colorParts = color.split(" ")
                    when (colorParts[1]) {
                        "red" -> maxR = maxOf(maxR, colorParts[0].toInt())
                        "green" -> maxG = maxOf(maxG, colorParts[0].toInt())
                        "blue" -> maxB = maxOf(maxB, colorParts[0].toInt())
                    }
                }
            }
            if (maxR <= 12 && maxG <= 13 && maxB <= 14) {
                sum1 += id.toInt()
            }
            sum2 += (maxR * maxG * maxB)
            ptl(maxR, maxG, maxB)
        }
        a(sum1, sum2)
    }
}
