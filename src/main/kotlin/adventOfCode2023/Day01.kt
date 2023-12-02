package adventOfCode2023
import util.*

@Suppress("unused")
class Day01(input: String, isTest : Boolean = false) : Day(input, isTest) {
    override fun solve() {
        var sum1 = 0
        var sum2 = 0
        val regex1 = Regex("[0-9]")
        val regex2 = Regex("[0-9]|one|two|three|four|five|six|seven|eight|nine")
        for (line in lines) {
            sum1 += (regex1.findAll(line).first().value +
                    regex1.findAll(line).last().value).toInt()

            val first = regex2.findAll(line).first().value
            var last = ""
            for (i in 0..line.length) {
                val match = regex2.matchAt(line, i)?.value
                last = match ?: last
            }
            sum2 += (strToDig(first).toString() +
                    strToDig(last).toString()).toInt()
        }
        a(sum1, sum2)
    }
    fun strToDig(str: String): Int {
        return when (str) {
            "one" -> 1
            "two" -> 2
            "three" -> 3
            "four" -> 4
            "five" -> 5
            "six" -> 6
            "seven" -> 7
            "eight" -> 8
            "nine" -> 9
            else -> str.toInt()
        }
    }
}
