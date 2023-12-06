package adventOfCode2023
import util.*

@Suppress("unused")
class Day06(input: String, isTest : Boolean = false) : Day(input, isTest) {
    fun howFar(holdTime: Long, raceTime: Long): Long {
        val goTime = raceTime - holdTime
        return goTime * holdTime
    }
    override fun solve() {
        // split both lines by spaces, combine into pairs
        val line1 = lines[0].split(" ").filter { it != ""}.filter { it[0] != 'T' }.map { it.toInt() }
        val line2 = lines[1].split(" ").filter { it != ""}.filter { it[0] != 'D' }.map { it.toInt() }
        val pairs = line1.zip(line2)
        // calculate ways
        var s1 = 1
        for (pair in pairs) {
            var sum = 0
            for (i in 1 until pair.first.toLong()) {
                val distance = howFar(i, pair.first.toLong())
                if (distance > pair.second) {
                    sum++
                }
            }
            s1 *= sum
        }
        a(s1)
        val line11 = lines[0].split(" ").filter { it != "" && it[0] != 'T' }.joinToString("").toLong()
        val line21 = lines[1].split(" ").filter { it != "" && it[0] != 'D' }.joinToString("").toLong()
        var sum = 0
        for (i in 1 until line11) {
            val distance = howFar(i, line11)
            if (distance > line21) {
                sum++
            }
        }
        a(sum)
    }
}
