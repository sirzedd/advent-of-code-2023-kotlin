import java.util.SortedMap
import kotlin.io.path.createTempDirectory

enum class HandType(val rank: Int) {
    FIVE(7), FOUR(6), FULL(5), THREE(4), TWO(3), ONE(2), HIGH(1);
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

    val mapToNextRankWithJoker = mapOf(
        HandType.HIGH to HandType.ONE,
        HandType.ONE to HandType.THREE,
        HandType.THREE to HandType.FOUR,
        HandType.FOUR to HandType.FIVE,
        HandType.TWO to HandType.FULL
    )
    fun getHandType(cards: List<Char>): HandType {
        val pairMap = findPairs(cards)
        val matches = pairMap.values
        return when {
            matches.contains(5) -> HandType.FIVE
            matches.contains(4) -> HandType.FOUR
            matches.contains(3) && matches.contains(2) -> HandType.FULL
            matches.contains(3) -> HandType.THREE
            matches.filter { it == 2 }.size == 2 -> HandType.TWO
            matches.contains(2) -> HandType.ONE
            else -> HandType.HIGH
        }
    }

    fun findHandTypeWithJoker(cardsBid: CardsBid): HandType {
        val (cards, jokers) = cardsBid.cards.partition { it != 'J' }
        val jokerCount = jokers.size
        val handType = getHandType(cards)

        return if (jokerCount == 0) {
            handType
        } else if (jokerCount == 5) {
            HandType.FIVE
        } else if (handType == HandType.TWO && jokerCount == 1) {
            HandType.FULL
        } else {
            var upgradingRank = handType
            for (i in 0 until jokerCount) {
                val nextRank = mapToNextRankWithJoker.get(upgradingRank)
                upgradingRank = nextRank!!
            }
            return upgradingRank
        }
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

    fun compareCards(o1: List<Char>, o2: List<Char>, cardValueCalculator: (a: Char)-> Int): Int {
        for (i in 0 .. o1.size) {
            val value1 = cardValueCalculator(o1[i])
            val value2 = cardValueCalculator(o2[i])
            if (value1 > value2) {
                return 1

            } else if (value1 < value2) {
                return -1

            }
        }
        return -1
    }

    fun calculateWinnings(currentRank: Int, cards: List<CardsBid>): Long {
        var winnings = 0L
        var currentRank = currentRank
        for (card in cards) {
            val winning = card.bid * currentRank
            winnings += winning
            currentRank += 1
        }
        return winnings
    }

    fun compareCardsAndCollectWinnings(cards: List<CardsBid>, startRank: Int, cardValue: (Char) -> Int): Long{
        val sortedCards = cards.sortedWith { o1, o2 ->
            compareCards(
                o1!!.cards,
                o2!!.cards,
                cardValue
            )
        }
        val winnings = calculateWinnings(startRank, sortedCards)

        return winnings
    }

    fun part1(inputs: List<String>): Long {
        val cardBids = inputs.map { parseCardBids(it) }
        val groupedByHandType = cardBids.groupBy { getHandType(it.cards) }
        var currentRank = 1
        var winnings = 0L

        val sortedByRank: SortedMap<HandType, List<CardsBid>?> = groupedByHandType.toSortedMap(compareBy { it.rank })

        for (cardsInRank in sortedByRank.values) {
            winnings += compareCardsAndCollectWinnings(cardsInRank!!, currentRank) {
                i -> cardValue(i)
            }
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
            winnings += compareCardsAndCollectWinnings(cards!!, currentRank) {i -> cardValueWithJoker(i)}
            currentRank += cards.size
        }

        return winnings
    }

    val testInput = readInput("Day07_test")
    println("--- Part 1 test ---")
    val part1Answer = part1(testInput)

    val part1Solution = 6440L
    if (part1Answer != part1Solution) {
        error("Part 1 test failed = $part1Answer != $part1Solution")
        return
    } else {
        println("Successful")
    }

    println("--- Part 1 ---")
    val input = readInput("Day07")
    part1(input).println()

    println("--- Part 2 test ---")
    val part2Answer = part2(testInput)
    val part2Solution = 5905L
    if (part2Answer != part2Solution) {
        error("Part 2 test failed = $part2Answer != $part2Solution")
        return
    } else {
        println("Successful")
    }
    println("--- Part 2 ---")
    part2(input).println()
}
