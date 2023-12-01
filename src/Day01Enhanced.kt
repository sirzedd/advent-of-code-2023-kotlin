import kotlin.io.path.Path

fun main() {

    //Kotin youtube solution, just use first and last but give it a condition based on digit 🧑‍🍳💋
    fun firstAndLastNumber(str: String): Int =
        "${str.first { it.isDigit() }}${str.last { it.isDigit() }}".toInt()



    fun part1(input: List<String>): Int =
        input.sumOf { firstAndLastNumber(it) }


    //What I was trying to do, after listening to the Kotlin channel
    //they mentioned something similar in somebodies solution
    // It will help keep word order
    val numStringMap = mapOf(
        "one" to "o1e",
        "two" to "t2o",
        "three" to "t3e",
        "four" to "f4r",
        "five" to "f5e",
        "six" to "s6x",
        "seven" to "s7n",
        "eight" to "e8t",
        "nine" to "n9e"
    )
    fun replaceAllNamedDigits(str: String): String {
        var newStr = str
        //go through 9 elements in map and replace all words in string
        numStringMap.forEach { k, v -> newStr = newStr.replace(k, v)  }
        return newStr
    }

    fun part2(input: List<String>): Int {
        val sum = input
            //turn string numbers into numbers
            .map { replaceAllNamedDigits(it) }
            //grab first and last
            .sumOf { firstAndLastNumber(it)}
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()


    val testInput2 = readInput("Day01_test2")
    check(value = part2(testInput2) == 281)
    part2(input).println()
}
