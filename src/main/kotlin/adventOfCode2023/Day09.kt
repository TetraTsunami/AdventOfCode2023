package adventOfCode2023
import util.*

@Suppress("unused")
class Day09(input: String, context: RunContext = RunContext.PROD) : Day(input, context) {
    override fun solve() {
        var s1 = 0
        var s2 = 0
        for (line in lines) {
            val words = line.split(" ").map { it.toInt() }
            var deriv = mutableListOf(words.zip(words.drop(1)).map { it.second - it.first })
            while (!deriv.last().all { it == 0}) {
                deriv.add(deriv.last().zip(deriv.last().drop(1)).map { it.second - it.first })
            }
            var next = 0
            for (derivs in deriv.reversed().drop(1)) {
                next += derivs.last()
            }
            s1 += next + words.last()

            next = 0
            for (derivs in deriv.reversed().drop(1)) {
                next = derivs.first() - next
                plt("$next $derivs")
            }
            plt("$next $words")
            s2 += words.first() - next
            plt()
        }
        a(s1, s2)
    }
}
