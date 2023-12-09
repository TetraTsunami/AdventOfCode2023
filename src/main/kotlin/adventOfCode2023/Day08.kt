package adventOfCode2023
import util.*

@Suppress("unused")
class Day08(input: String, isTest : Boolean = false) : Day(input, isTest) {
    override fun solve() {
        var nodes = mutableMapOf<String, Pair<String, String>>()
        for (line in lines.subList(2, lines.size)) {
            val words = line.replace(Regex("[=(),]"), "").split(" ").filter { it != "" }
            nodes[words[0]] = Pair(words[1], words[2])
        }
        var directions = lines[0].toCharArray()
        a(getSolutionNode("AAA", directions, nodes))
        a(nodes.keys.filter { it[2] == 'A' }.map { getSolutionNode(it, directions, nodes) }
            .reduce { acc, i -> findLCM(acc, i) })
    }

    fun getSolutionNode(node: String, directions: CharArray, nodes: Map<String, Pair<String, String>>): Long {
        pt("Checking $node: ")
        var i = 0L
        var cur = node
        while (cur[2] != 'Z') {
            val dir = directions[(i % directions.size).toInt()]
            val next = nodes[cur]!!
            cur = if (dir == 'L') {
                next.first
            } else {
                next.second
            }
            i++
        }
        ptl(i)
        return i
    }

    fun findLCM(a: Long, b: Long): Long {
        pt("Finding LCM of $a and $b: ")
        val larger = if (a > b) a else b
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                ptl(lcm)
                return lcm
            }
            lcm += larger
        }
        ptl(maxLcm)
        return maxLcm
    }
}
