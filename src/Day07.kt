import java.util.SortedMap

enum class HandType(val rank: Int) {
    FIVE(7), FOUR(6), FULL(5), THREE(4), TWO(3), ONE(2), HIGH(1);

    companion object {
        fun fromInt(value: Int) = HandType.values().first { it.rank == value }
    }
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

    val mapToNextRank = mapOf(
        HandType.HIGH to HandType.ONE,
        HandType.ONE to HandType.THREE,
        HandType.THREE to HandType.FOUR,
        HandType.FOUR to HandType.FIVE,
        HandType.TWO to HandType.FULL
    )

    fun findHandTypeWithJoker(cardsBid: CardsBid): HandType {
        val cards = cardsBid.cards
        val pairMap = findPairs(cardsBid.cards)
        val jokers = pairMap.getOrDefault('J', 0)
        val cardMapWithNoJokers = pairMap.filter { it.key != 'J' }
        val matches = cardMapWithNoJokers.values

        /*
            Does joker just move up handType
            HIGH -> ONE -> THREE -> FOUR -> FIVE
            TWO -> FULL

           HIGH=[3, 2, T, 4, K]
           HIGH=[3, 2, T, 4, J] -> ONE
           ONE=[3, 3, T, 4, J] -> THREE
           TWO=[3, 3, 4, 4, J] -> Full
           Three=[3,3,3,4,J] -> FOUR
           Full=[3,3,3,4,4] -> Can't happen
           Four=[4,4,4,4,J] -> FIVE
           FIVE=[4,4,4,4,4] -> Can't happen
           HIGH=[3,2,T,J,J] -> THREE
           THREE=[3,3,3,J,J] -> FIVE
           ONE=[2,2,J,J,J] -> FIVE
           HIGH=[1,J,J,J,J] -> FIVE
         */
       var handType: HandType = if (matches.size == cardsBid.cards.size) {
            HandType.HIGH
        } else if (matches.contains(5)) {
            HandType.FIVE
        } else if (matches.contains(4)) {
            HandType.FOUR
        } else if (matches.contains(3) && matches.contains(2)) {
            HandType.FULL
        } else if (matches.contains(3)) {
            HandType.THREE
        } else if (matches.contains(2)) {
           if (matches.groupBy { it }.get(2)!!.size == 2) {
                HandType.TWO
            } else {
                HandType.ONE
           }
        } else {
            HandType.HIGH
       }

        return if (jokers == 0) {
            handType
        } else if (jokers == 5) {
            HandType.FIVE
        } else if (handType == HandType.TWO && jokers == 1) {
            HandType.FULL
        } else {
            for (i in 0 until jokers) {
                val nextRank = mapToNextRank.get(handType)
                handType = nextRank!!
            }
            return handType
        }
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
    fun cardValueWithJoker(i: Char): Int {
        return when (i) {
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'J' -> 1
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
    fun compareCardsWithJokers(o1: List<Char>, o2: List<Char>): Int {
        for (i in 0 .. o1.size) {
            val c1 = o1[i]
            val c2 = o2[i]
            val value1 = cardValueWithJoker(o1[i])
            val value2 = cardValueWithJoker(o2[i])
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
    fun compareCardsAndCollectWinningsWithJokers(cards: List<CardsBid>, startRank: Int): Long{
        var currentRank = startRank
        val sortedCards = cards.sortedWith(object: Comparator<CardsBid> {
            override fun compare(o1: CardsBid?, o2: CardsBid?): Int {
                return compareCardsWithJokers(o1!!.cards, o2!!.cards)
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
        val cardBids = inputs.map { parseCardBids(it) }
        val groupedByHandType = cardBids.groupBy { findHandTypeWithJoker(it) }
        var currentRank = 1
        var winnings = 0L

        val sortedByRank: SortedMap<HandType, List<CardsBid>?> = groupedByHandType.toSortedMap(compareBy { it.rank })

        for (rankAndCards in sortedByRank) {
            val cards = rankAndCards.value
            val rank = rankAndCards.key
            println("Rank $rank")
            winnings += compareCardsAndCollectWinningsWithJokers(cards!!, currentRank)
            currentRank += cards.size
        }

        return winnings
    }
//
    val testInput = readInput("Day07_test")
//    val part1Answer = part1(testInput)
//
//    val part1Solution = 6440L
//    if (part1Answer != part1Solution) {
//        error("Part 1 test failed = $part1Answer != $part1Solution")
//        return
//    }
    println("---")
    val input = readInput("Day07")
//    part1(input).println()

//    val testInput2 = readInput("Day07_test")
    val part2Answer = part2(testInput)
    val part2Solution = 5905L
    if (part2Answer != part2Solution) {
        error("Part 2 test failed = $part2Answer != $part2Solution")
        return
    }
    println("---")
    part2(input).println()
}
