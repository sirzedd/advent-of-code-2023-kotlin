//My initial attempt
fun main() {

    data class Number(
        var number: Int = 0,
        val row: Int,
        var indexRange: IntRange? = null
    )

    data class Schematics(
        val numbers: MutableList<Number> = mutableListOf(),
        var symbolMap: MutableMap<Pair<Int, Int>, Char> = mutableMapOf()
    )

    //Parse into schematics, putting characters into map for quick lookup
    //numbers have ranges, can have duplicates
    fun parseSchematics(input: String, row: Int, schematics: Schematics) : Schematics {
        var number = Number(row = row)
        val numbers = mutableListOf<Number>()
        val symbolMap = mutableMapOf<Pair<Int, Int>, Char>()
        for (i in 0 until input.length) {
            val ch = input[i]
            if (ch.isDigit()) {
                number.number = number.number * 10 + ch.digitToInt()

                if (number.indexRange == null) {
                    number.indexRange = IntRange(i, i + 1)
                } else {
                    number.indexRange = IntRange(number.indexRange!!.start, i + 1)
                }
            }
            //reset and add if not a digit and we have an indexRange
            if (!ch.isDigit() && number.indexRange != null) {
                numbers += number
                number = Number(row = row)
            }

           //add special symbol
            if (ch != '.' && !ch.isDigit()) {
               symbolMap += (row to i) to ch
            }
        }


        //add any at the end
        if (number.indexRange != null) {
            numbers += number
        }

        schematics.numbers += numbers
        schematics.symbolMap +=  symbolMap

        return schematics
    }

    //check if the symbol is in range of the number
    //based on number row and specific range
    fun checkForSymbol(row: Int, range: IntRange, symbolMap: MutableMap<Pair<Int, Int>, Char>): Boolean {
        for(i in range step 1) {
            if (symbolMap.contains(row to i)) {
                return true
            }
        }
        return false
    }

    fun getNumberIfItIsTouchingSymbol(number: Number, symbolMap: MutableMap<Pair<Int, Int>, Char>): Int {
        val row = number.row
        val range = number.indexRange!!

        if(
            //check before number range same row
            checkForSymbol(row, IntRange(range.start - 1, range.start), symbolMap) ||

            //check after number range same row
            checkForSymbol(row, IntRange(range.last-1, range.last), symbolMap) ||

            //below range row--, -1, +1 wider range to include diagonals
            checkForSymbol(row - 1, IntRange(range.first - 1, range.last), symbolMap) ||

            //above range row ++, -1, +1 wider range to include diagonals
            checkForSymbol(row + 1, IntRange(range.first - 1, range.last), symbolMap)
        ) {
            //return number if any are true
            return number.number
        }
        return 0


    }

    fun part1(inputs: List<String>): Int =
        inputs.foldRightIndexed(Schematics()) { index, s, schematics -> parseSchematics(s, index, schematics) }
            .let { s -> s.numbers.sumOf { getNumberIfItIsTouchingSymbol(it, s.symbolMap) } }

    fun findGearsValue(gear: Map.Entry<Pair<Int, Int>, Char>, numbers: List<Number>): Int {
       val touchingNumbers = mutableListOf<Int>()
        for (number in numbers) {
            //for each number
            if (getNumberIfItIsTouchingSymbol(number, mutableMapOf(gear.key to gear.value)) != 0) {
                //this number is touching the symbol, add to list
                touchingNumbers += number.number
            }
        }
        if (touchingNumbers.size == 2) {
            //This applies only for 2 number parts
            //multiply them together
            return touchingNumbers[0] * touchingNumbers[1]
        }
        //If they don't 0
        return 0
    }


    fun part2(inputs: List<String>): Int =
        inputs.foldRightIndexed(Schematics()) { index, s, schematics -> parseSchematics(s, index, schematics) }
            .let { s ->
                //filter only * symbols for gears
                s.symbolMap.filter { entry -> entry.value == '*' }
                    //sum them all up
                    .entries.sumOf {  findGearsValue(it, s.numbers)  }
            }

    val testInput = readInput("Day03_test")
    val part1Answer = part1(testInput)
    check(part1Answer == 4361)

    val testInput1_2 = readInput("Day03_test1-2")
    val part1Answer1_2 = part1(testInput1_2)
    check(part1Answer1_2 == 5)
//
    val input = readInput("Day03")
    part1(input).println()
//
    val testInput2 = readInput("Day03_test2")
    val part2Answer = part2(testInput2)
    check(part2Answer == 467835)
//
    val testInput2_2 = readInput("Day03_test2-2")
    val part2Answer_2 = part2(testInput2_2)
    check(part2Answer_2 == 2)

    part2(input).println()
}
