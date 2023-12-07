import java.util.SortedMap

enum class HandType(val rank: Int) {
    FIVE(7), FOUR(6), FULL(5), THREE(4), TWO(3), ONE(2), HIGH(1)
}
//My initial attempt
fun main() {

    data class CardsBid(
        val cards: List<Char>,
        val bid: Long,
        var handType: String = "",
    )
    //32T3K 765
    fun parseCardBids(input: String): CardsBid {
        val (cardsString, bid) = input.split(" ").filter { it.isNotBlank() }
        val cards = cardsString.toCharArray().toList()
        return CardsBid(cards, bid.toLong())
    }

    fun findPairs(cards: List<Char>) : Map<Char, Int> {
        val pairMap: MutableMap<Char, Int> = mutableMapOf()
        for (ch in cards) {
            val matches = pairMap.getOrDefault(ch, 0) + 1
            pairMap += ch to matches
        }
        return pairMap
    }
    fun findHandType(cardsBid: CardsBid): HandType {
        val pairMap = findPairs(cardsBid.cards)
        val matches = pairMap.values
        if (matches.size == cardsBid.cards.size) {
            return HandType.HIGH
        } else if (matches.contains(5)) {
            return HandType.FIVE
        } else if (matches.contains(4)) {
            return HandType.FOUR
        } else if (matches.contains(3) && matches.contains(2)) {
            return HandType.FULL
        } else if (matches.contains(3)) {
            return HandType.THREE
        } else if (matches.contains(2)) {
           if (matches.groupBy { it }.get(2)!!.size == 2) {
                return HandType.TWO
            } else {
                return HandType.ONE
           }
        }

        return HandType.HIGH
    }

    fun cardValue(i: Char): Int {
       return when (i) {
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'J' -> 11
            'T' -> 10
            else -> "$i".toInt()
        }
    }

    fun compareCards(o1: List<Char>, o2: List<Char>): Int {
        for (i in 0 .. o1.size) {
           val value1 = cardValue(o1[i])
            val value2 = cardValue(o2[i])
            if (value1 > value2) {
                return 1

            } else if (value1 < value2) {
                return -1

            }
        }
        return -1
    }


    fun compareCardsAndCollectWinnings(cards: List<CardsBid>, startRank: Int): Long{
        var currentRank = startRank
        val sortedCards = cards.sortedWith(object: Comparator<CardsBid> {
            override fun compare(o1: CardsBid?, o2: CardsBid?): Int {
                return compareCards(o1!!.cards, o2!!.cards)
            }
        })
        var winnings = 0L
        for (card in sortedCards) {
            val winning = card.bid * currentRank
            println("${card.cards} = ${card.bid} * $currentRank = ${winning}")
            winnings += winning
            currentRank += 1
        }

        return winnings
    }

    fun part1(inputs: List<String>): Long {
        val cardBids = inputs.map { parseCardBids(it) }
        val groupedByHandType = cardBids.groupBy { findHandType(it) }
        var currentRank = 1
        var winnings = 0L

        val sortedByRank: SortedMap<HandType, List<CardsBid>?> = groupedByHandType.toSortedMap(compareBy { it.rank })

        for (cardsInRank in sortedByRank.values) {
            winnings += compareCardsAndCollectWinnings(cardsInRank!!, currentRank)
            currentRank += cardsInRank.size
        }

        return winnings
    }

    fun part2(inputs: List<String>): Long {
        return 0L
    }

    val testInput = readInput("Day07_test")
    val part1Answer = part1(testInput)

    val part1Solution = 6440L
    if (part1Answer != part1Solution) {
        error("Part 1 test failed = $part1Answer != $part1Solution")
        return
    }

    val input = readInput("Day07")
    part1(input).println()

//    val testInput2 = readInput("Day07_test")
//    val part2Answer = part2(testInput2)
//    check(part2Answer == 71503L)
//    part2(input).println()
}
