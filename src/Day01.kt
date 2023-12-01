fun main() {
    
    fun addTwoNums(input: String): Int {
        var lo: Char? = null
        var hi: Char? = null

        for (ch in input) {
            if (ch.isDigit()) {
                if (lo == null) {
                    lo = ch
                }
                hi = ch
            }
        }
        return "$lo$hi".toInt()
    }

    fun part1(input: List<String>): Int {
        val sum = input.map { addTwoNums(it) }.sum()
        return sum
    }



    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()
//    part2(input).println()
}
