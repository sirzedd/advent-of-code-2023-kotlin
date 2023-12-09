fun main() {
    fun parseSensorReading(input: String): List<Long> {
        val row = input.split(" ")
            .filter { it.isNotBlank() }
            .map { it.toLong() }
            .reversed() //reversed so subtraction is easier and what i'm looking for is the first two numbers
        return row
    }

    fun nextValue(readings: List<Long>, nextMap : MutableMap<Pair<Long, Long>, Long>, operation: (Long, Long)-> Long): Long {
        if (readings.filter { it == 0L }.size == readings.size) {
            //when they are all 0, return next 0
            return 0L
        }
        val first = readings[0]
        val second = readings[1]
        if (nextMap.contains(first to second)) {
            return nextMap.getValue(first to second)
        }

        val nextRow = readings.windowed(2, 1)
            .map { it[0] - it[1] }
        val valueBelow = nextValue(nextRow, nextMap, operation)
        val nextValue = operation(first, valueBelow)
        nextMap += (first to second) to nextValue

        return nextValue
    }

    fun part1(inputs: List<String>): Long {
        val readings = inputs.map { parseSensorReading(it) }

        val total = readings.sumOf{
            val nextMap : MutableMap<Pair<Long, Long>, Long> = mutableMapOf()
            nextValue(it, nextMap) {a, b -> a + b} }

        return total
    }

    fun part2(inputs: List<String>): Long {
        val readings = inputs.map { parseSensorReading(it) }

        val total = readings.sumOf{
            val nextMap : MutableMap<Pair<Long, Long>, Long> = mutableMapOf()
            nextValue(it.reversed(), nextMap) {a, b -> a - b} }

        return total
    }

    test( "Part 1 test", "Day09_test", 114L,  ) {i -> part1(i)}
    test( "Part 1 - 2 test", "Day09_test1-2", -1586L,  ) {i -> part1(i)}


    println("--- Part 1 ---")
    val input = readInput("Day09")
    part1(input).println()

    test( "Part 2 test", "Day09_test2", 5L,  ) {i -> part2(i)}
//    println("--- Part 2 test ---")
//    val testInput2 = readInput("Day09_test2")
//    val part2Answer = part2(testInput2)
//    val part2Solution = 6L
//    if (part2Answer != part2Solution) {
//        error("Part 2 test failed = $part2Answer != $part2Solution")
//        return
//    } else {
//        println("Successful")
//    }

//    println("--- Part 2 ---")
//    part2(input).println()
}


fun test(partName: String, testFile: String, answer: Long, part: (inputs: List<String>)-> Long) {
    val testInput = readInput(testFile)
    println("--- $partName ---")
    val partAnswer = part(testInput)

    if (partAnswer != answer) {
        error("$partName failed = $partAnswer != $answer")
        return
    } else {
        println("Successful")
    }
    println("--- $partName end---")}