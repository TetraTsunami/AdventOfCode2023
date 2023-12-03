package adventOfCode2023
import util.*
import util.Constants.zeroToNine

@Suppress("unused")
class Day01(input: String, isTest : Boolean = false) : Day(input, isTest) {
    override fun solve() {
        val regex1 = Regex("[0-9]")
        lines.map {line ->
            (regex1.findAll(line).first().value +
                    regex1.findAll(line).last().value).toInt() to

            line.mapIndexed { index, c -> c.digitToIntOrNull() ?:
                                          line.substring(index).let { subst ->
                                              zeroToNine.indexOfFirst { digit -> subst.startsWith(digit) }}}
                .filter { it != -1 }
                .let { it.first() * 10 + it.last() }
        }.reduce { (a1, a2), (b, c) -> a1 + b to a2 + c }.let { (p1, p2) -> a(p1, p2) }
    }
}
