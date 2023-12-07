package adventOfCode2023
import util.*

@Suppress("unused")
class Day07(input: String, isTest : Boolean = false) : Day(input, isTest) {
    fun getFreqMap(hand: Pair<String, Int>): Map<Char, Int> {
        val freqMap = mutableMapOf<Char, Int>()
        var j = 0
        for (i in 0..4) {
            val card = hand.first[i]
            if (card == 'J') {
                j++
            } else {
                val freq = freqMap.getOrDefault(card, 0)
                freqMap[card] = freq + 1
            }
        }
        val mostCommon = freqMap.entries.maxByOrNull { it.value }
        if (mostCommon == null) {
            freqMap['A'] = freqMap.getOrDefault('A', 0) + j
        } else {
            freqMap[mostCommon.key] = freqMap.getOrDefault(mostCommon.key, 0) + j
        }
        return freqMap
    }
    fun pickType(hand: Pair<String, Int>): Int {
        val freqs = getFreqMap(hand).values.sortedDescending()
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
    fun compareHands(hand1: Pair<String, Int>, hand2: Pair<String, Int>): Int {
        val type1 = pickType(hand1)
        val type2 = pickType(hand2)
        if (type1 != type2) {
            return type1 - type2
        }
        // highest card
        // a > k > q > j > t > 9 > 8...
        val valueMap = mapOf('A' to 14, 'K' to 13, 'Q' to 12, 'J' to 1, 'T' to 10)
        for (i in 0..4) {
            if (hand1.first[i] != hand2.first[i]) {
                return valueMap.getOrDefault(hand1.first[i], hand1.first[i].digitToIntOrNull())!! -
                        valueMap.getOrDefault(hand2.first[i], hand2.first[i].digitToIntOrNull())!!
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
        val valueMap = mapOf('A' to 14, 'K' to 13, 'Q' to 12, 'J' to 10, 'T' to 10)
        for (i in 0..4) {
            if (hand1.first[i] != hand2.first[i]) {
                return valueMap.getOrDefault(hand1.first[i], hand1.first[i].digitToIntOrNull())!! -
                        valueMap.getOrDefault(hand2.first[i], hand2.first[i].digitToIntOrNull())!!
            }
        }
        return 0
    }
}
