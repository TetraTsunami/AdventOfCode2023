package adventOfCode2023
import util.*

@Suppress("unused")
class Day05(input: String, context: RunContext = RunContext.PROD) : Day(input, context) {
    data class RangeMap(val destStart: Long, val srcStart: Long, val rangeLen: Long) {
        val srcEnd = srcStart + rangeLen
        val destEnd = destStart + rangeLen

        fun isInRange(index: Long): Boolean {
            return index >= srcStart && index < srcStart + rangeLen
        }
        fun hasOverlap(seedRange: SeedRange): Boolean {
           return seedRange.start < srcEnd && seedRange.end > srcStart
        }

        fun getDestIndex(srcIndex: Long): Long {
            return destStart + (srcIndex - srcStart)
        }
        fun getSrcIndex(destIndex: Long): Long {
            return srcStart + (destIndex - destStart)
        }

        fun getDestRange(seedrng: SeedRange): SeedRange {
            // effectively finding overlap of two ranges
            // find starting index of overlap
            val start = if (seedrng.start < srcStart) srcStart else seedrng.start
            // find ending index of overlap
            val end = if (seedrng.end > srcEnd) srcEnd else seedrng.end
            // return new range with overlap
            return SeedRange(getDestIndex(start), end - start)
        }
        fun getDestIndexRemainder(seedrng: SeedRange): List<SeedRange> {
            // finding non-overlap of two ranges
            // could be 0, 1, or 2 ranges
            val overlap = this.getDestRange(seedrng)
            var overlapStart = this.getSrcIndex(overlap.start)
            var overlapEnd = this.getSrcIndex(overlap.end)
            val remainder = mutableListOf<SeedRange>()
            if (seedrng.start < overlapStart) {
                remainder.add(SeedRange(seedrng.start, overlapStart - seedrng.start))
            }
            if (seedrng.end > overlapEnd) {
                remainder.add(SeedRange(overlapEnd, seedrng.end - overlapEnd))
            }
            return remainder
        }
    }
    data class SeedRange(val start: Long, val length: Long) {
        val end = start + length
        fun isInRange(index: Long): Boolean {
            return index >= start && index < start + length
        }
        fun getDestIndex(srcIndex: Long): Long {
            return start + (srcIndex - start)
        }
        fun toPair(): Pair<Long, Long> {
            return Pair(start, length)
        }
    }
    override fun solve() {
        run {
            val seeds =
                lines[0].split(" ").subList(1, lines[0].split(" ").size).map { it.toLong() }
            var i = 3
            var lastSeeds = seeds.toMutableList()
            var thisSeeds = mutableListOf<Long>()
            while (i < lines.size) {
                val line = lines[i]
                if (line == "") {
                    i += 2
                    thisSeeds.addAll(lastSeeds)
                    lastSeeds = thisSeeds.toMutableList()
                    thisSeeds.clear()
                    continue
                }
                val words = line.split(" ")
                val map = RangeMap(
                    words[0].toLong(),
                    words[1].toLong(),
                    words[2].toLong()
                )
                val temp = lastSeeds.toList()
                for (seed in temp) {
                    if (map.isInRange(seed)) {
                        thisSeeds.add(map.getDestIndex(seed))
                        lastSeeds.remove(seed)
                    }
                }
                i++
            }
            thisSeeds.addAll(lastSeeds)
            a(thisSeeds.min())
        }
        // pairs of (start, length)
        val seeds: List<SeedRange> =
            lines[0].split(" ").subList(1, lines[0].split(" ").size).map { it.toLong() }
                .chunked(2).map { SeedRange(it[0], it[1]) }
        var i = 3
        var lastSeeds = seeds.toMutableList()
        var thisSeeds = mutableListOf<SeedRange>()
        while (i < lines.size) {
            val line = lines[i]
            if (line == "") {
                i += 2
                thisSeeds.addAll(lastSeeds)
                lastSeeds = thisSeeds.toMutableList()
                thisSeeds.clear()
                continue
            }
            val words = line.split(" ")
            val map = RangeMap(
                words[0].toLong(),
                words[1].toLong(),
                words[2].toLong()
            )
            val temp = lastSeeds.toList()
            for (seedRange in temp) {
                lastSeeds.remove(seedRange)
                if (map.hasOverlap(seedRange)) {
                    thisSeeds.add(map.getDestRange(seedRange))
                    lastSeeds.addAll(map.getDestIndexRemainder(seedRange))
                } else {
                    lastSeeds.add(seedRange)
                }
            }
            i++
        }
        thisSeeds.addAll(lastSeeds)
        a(thisSeeds.minBy { it.start }.start)

    }
}
