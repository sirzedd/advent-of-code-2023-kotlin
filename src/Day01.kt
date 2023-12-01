import kotlin.io.path.Path

fun main() {

    fun addTwoNums(input: String): Int {
        var lo: Char? = null
        var hi: Char? = null


        //Go through all characters, one pass
        for (ch in input) {
            if (ch.isDigit()) {
                //if digit is found
                if (lo == null) {
                    //only replace lo once, it'll be at lowest
                    lo = ch
                }
                //keep replacing hi, it'll be the highest
                hi = ch
            }
        }

        
        //combine numbers to int
        return "$lo$hi".toInt()
    }

    fun part1(input: List<String>): Int {
        val sum = input.map { addTwoNums(it) }.sum()
        return sum
    }

    val numStringMap = mapOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9"
    )
    fun replaceAllNamedDigits(str: String): String {
        //build new string
        var newStr = ""
        for (i in 0 until str.length) {
            //go through all
            for (j in i until str.length) {
                //checking for name found in map
                val m = str.subSequence(i, j+1)
                val value = numStringMap.get(m.toString())
                if (value != null) {
                    //if found add value to string
                    newStr += value
                    break;
                }
                val matchList = numStringMap.keys.filter { it.startsWith(m) }
                //if this matches keep going through j
                if (matchList.isEmpty()) {
                    //if no match, add sequence and break
                    newStr += m
                    break;
                }
            }
        }
        //new string contains correct digits and a little extra cruft
        return newStr
    }

    fun replaceStringToNum(str: String): String {
        var s = str
        s = replaceAllNamedDigits(s)
        return s
    }


    fun part2(input: List<String>): Int {
        val sum = input
            .map { replaceStringToNum(it) }
            .map { addTwoNums(it) }.sum()
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
