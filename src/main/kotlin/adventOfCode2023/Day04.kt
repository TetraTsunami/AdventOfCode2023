package adventOfCode2023
import util.*

@Suppress("unused")
class Day04(input: String, isTest : Boolean = false) : Day(input, isTest) {
    override fun solve() {
        var s1 = 0
        val cardCollection = mutableMapOf(0 to 1)
        for ((i, _) in lines.withIndex()) {
            cardCollection[i] = 1
        }
        for ((i, line) in lines.withIndex()) {
            val words = line.split(" ")
            var divider = words.indexOf("|")
            var winningNumbers = words.subList(2, divider)
            var ourNumbers = words.subList(divider + 1, words.size).filter { it != "" }
            var matches = 0
            for (num in winningNumbers) {
                if (ourNumbers.indexOf(num) != -1) {
                    matches = if (matches == 0) 1 else matches * 2
                }
            }
            s1 += matches

            processCard(i, lines).forEach { cardCollection[it] = cardCollection[it]!!.plus(cardCollection[i]!!) }
        }
        a(s1, cardCollection.asIterable().sumOf { it.value })
    }

    fun processCard(lineNo: Int, lines: List<String>): List<Int> {
        val line = lines[lineNo]
        var words = line.split(" ")
        var divider = words.indexOf("|")
        var winningNumbers = words.subList(2, divider)
        var ourNumbers = words.subList(divider + 1, words.size).filter { it != "" }
        var matches = winningNumbers.filter { ourNumbers.indexOf(it) != -1 }.size
        return (lineNo + 1.. lineNo + matches).toList()
    }
}
