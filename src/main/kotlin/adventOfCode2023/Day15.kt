package adventOfCode2023
import util.*

@Suppress("unused")
class Day15(input: String, context: RunContext = RunContext.PROD) : Day(input, context) {
    override fun solve() {
        val steps = input.split(',')
        a(steps.sumOf { asciihash(it) }) // 519603
        val boxes = List(256) { mutableListOf<Pair<String, Int>>() }
        for (step in steps) {
            val label = step.takeWhile { it.isLetter() }
            val operation = step.drop(label.length).first()
            val operand = step.drop(label.length + 1).toIntOrNull()
            when (operation) {
                '=' -> {
                    val pre = boxes[asciihash(label)].indexOfFirst { it.first == label }
                    if (pre != -1) {
                        boxes[asciihash(label)][pre] = Pair(label, operand!!)
                    } else {
                        boxes[asciihash(label)].add(Pair(label, operand!!))
                    }
                }
                '-' -> {
                    boxes[asciihash(label)].removeIf { it.first == label }
                }
            }
        }
        var s2 = 0
        for ((i, box) in boxes.withIndex()) {
            if (box.isNotEmpty()) pt("Box ${i + 1}:")
            for ((j, lens) in box.withIndex()) {
                pt(" [${lens.first} ${lens.second}]")
                s2 += (i + 1) * (j + 1) * lens.second
            }
            if (!box.isEmpty()) plt()
        }
        a(s2) //244342
    }

    fun asciihash(input: String): Int {
        var cur = 0
        for (c in input) {
            cur = ((c.code + cur) * 17) % 256
        }
        // plt("$input -> $cur")
        return cur
    }
}
