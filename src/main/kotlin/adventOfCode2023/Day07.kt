package adventOfCode2023
import util.*

@Suppress("unused")
class Day07(input: String, isTest : Boolean = false) : Day(input, isTest) {
    fun getFreqMap(hand: Pair<String, Int>): Map<Char, Int> {
        val freqMap = mutableMapOf<Char, Int>()
        var j = 0
        for (i in 0..4) {
            val card = hand.first[i]
            if (card == 'J') j++
            else {
                freqMap[card] = freqMap.getOrDefault(card, 0) + 1
            }
        }
        // joker logic
        if (freqMap.isEmpty()) {
            freqMap['A'] = j
        } else {
            val mc = freqMap.entries.maxBy { it.value }
            freqMap[mc.key] = freqMap.getOrDefault(mc.key, 0) + j
        }
        return freqMap
    }
    fun pickType(hand: Pair<String, Int>): Int {
        val freqs = getFreqMap(hand).values.sortedDescending()
        when (freqs[0]) {
            5 -> return 6
            4 -> return 5
            3 -> {
                if (freqs[1] == 2) return 4
                return 3
            }
            2 -> {
                if (freqs[1] == 2) return 2
                return 1
            }
        }
        return 0
    }
    fun compareHands(h1: Pair<String, Int>, h2: Pair<String, Int>): Int {
        val type1 = pickType(h1)
        val type2 = pickType(h2)
        val c1 = h1.first
        val c2 = h2.first
        if (type1 != type2) {
            return type1 - type2
        }
        val valueMap = mapOf('A' to 14, 'K' to 13, 'Q' to 12, 'J' to 1, 'T' to 10)
        for (i in 0..4) {
            if (c1[i] != c2[i]) {
                return valueMap.getOrDefault(c1[i], c1[i].digitToIntOrNull())!! -
                        valueMap.getOrDefault(c2[i], c2[i].digitToIntOrNull())!!
            }
        }
        return 0
    }
    override fun solve() {
        var hands = mutableListOf<Pair<String, Int>>()
        for (line in lines) {
            val split = line.split(" ")
            hands.add(Pair(split[0], split[1].toInt()))
        }
        // care about it.first = poker rules
        var sum1 = 0
        var sum2 = 0
        val pt1hands = hands.toMutableList()
        pt1hands.sortWith { o1, o2 -> compareHandsPt1(o1, o2) }
        for ((i, hand) in pt1hands.withIndex()) {
            sum1 += (i + 1) * hand.second
        }
        a(sum1)
        hands.sortWith { o1, o2 -> compareHands(o1, o2) }
        for ((i, hand) in hands.withIndex()) {
            sum2 += (i + 1) * hand.second
        }
        a(sum2)
    }

    // Extra functions to solve part 1 and part 2 separately
    fun getFreqMapPt1(hand: Pair<String, Int>): Map<Char, Int> {
        val freqMap = mutableMapOf<Char, Int>()
        for (i in 0..4) {
            val card = hand.first[i]
            val freq = freqMap.getOrDefault(card, 0)
            freqMap[card] = freq + 1
        }
        return freqMap
    }
    fun pickTypePt1(hand: Pair<String, Int>): Int {
        val freqs = getFreqMapPt1(hand).values.sortedDescending()
        if (freqs[0] == 5) {
            return 6
        }
        if (freqs[0] == 4) {
            return 5
        }
        if (freqs[0] == 3) {
            if (freqs[1] == 2) {
                return 4
            }
            return 3
        }
        if (freqs[0] == 2) {
            if (freqs[1] == 2) {
                return 2
            }
            return 1
        }
        return 0
    }
    fun compareHandsPt1(hand1: Pair<String, Int>, hand2: Pair<String, Int>): Int {
        val type1 = pickTypePt1(hand1)
        val type2 = pickTypePt1(hand2)
        if (type1 != type2) {
            return type1 - type2
        }
        // highest card
        // a > k > q > j > t > 9 > 8...
        val valueMap = mapOf('A' to 14, 'K' to 13, 'Q' to 12, 'J' to 11, 'T' to 10)
        for (i in 0..4) {
            if (hand1.first[i] != hand2.first[i]) {
                return valueMap.getOrDefault(hand1.first[i], hand1.first[i].digitToIntOrNull())!! -
                        valueMap.getOrDefault(hand2.first[i], hand2.first[i].digitToIntOrNull())!!
            }
        }
        return 0
    }
}
