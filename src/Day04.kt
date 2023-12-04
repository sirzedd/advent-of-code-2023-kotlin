import kotlin.math.pow

//My initial attempt
fun main() {


    data class Card(
        val id: Int,
        val winningNumbers: List<Int>,
        val myNumbers: List<Int>
    )

    fun parseCard(input: String): Card {
        val cardNumbers = input.split(":")
        val cardId = cardNumbers[0].trim()
            .split(" ")
            .filter{it.isNotEmpty()}[1]
            .trim()
            .toInt()

        val winningMine = cardNumbers[1]
            .trim()
            .split("|")
        val winningNumbers = winningMine[0]
            .split(" ")
            .filter{it.isNotEmpty()}
            .map { it.trim().toInt() }
        val myNumbers = winningMine[1].trim().split(" ")
                .filter{ !it.isEmpty()}
                .map { it.trim().toInt() }
        return Card(
            id = cardId,
            winningNumbers,
            myNumbers
        )
    }

    fun scoreCard(card: Card): Int {
        val matching = card.winningNumbers.filter { card.myNumbers.contains(it) }
        if (matching.isEmpty()) {
            return 0;
        }
        val score = 2.0.pow(matching.size - 1).toInt()

        return score
    }


    fun part1(inputs: List<String>): Int =
        inputs.map { parseCard(it) }
            .sumOf { scoreCard(it) }

    fun part2(inputs: List<String>): Int {
        val cards = inputs.map { parseCard(it) }

        val copies: MutableList<Int> = MutableList(cards.size) {1}

        for (i in 0 until cards.size) {
            val number = copies[i]
            val card = cards[i]
            val matching = card.winningNumbers.filter { card.myNumbers.contains(it) }
            for (j in i+ 1 until i + matching.size + 1) {
                copies[j] += 1 * number
            }
        }
        return copies.sum()
    }

    val testInput = readInput("Day04_test")
    val part1Answer = part1(testInput)
    check(part1Answer == 13 )

    val input = readInput("Day04")
    part1(input).println()
//
    val testInput2 = readInput("Day04_test2")
    val part2Answer = part2(testInput2)
    check(part2Answer == 30)
    part2(input).println()
}
