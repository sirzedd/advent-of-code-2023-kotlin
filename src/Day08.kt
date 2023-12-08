fun main() {

    data class MapDirections(
        val direction: ArrayDeque<Char>,
        val map: MutableMap<String, Pair<String, String>>
    )

    fun parseMap(inputs: List<String>): MapDirections {
        val directions: ArrayDeque<Char> = inputs.first()
            .toCharArray()
            .foldRight(ArrayDeque()) { c, acc ->
                acc.addLast(c)
                acc
            }
        //AAA = (BBB, CCC)
        val map: MutableMap<String, Pair<String, String>> = inputs.subList(2, inputs.size)
            .fold(mutableMapOf()) { acc, s ->
                val (key, left, right) =
                s.replace("(","")
                    .replace(")","")
                    .split("=", ",")
                acc.put(key.trim(), left.trim() to right.trim())
                acc
            }
        return MapDirections(directions, map)
    }

    fun findStepsToEnd(directionMap: MapDirections, start: String, end: String): Long {
        val directions = directionMap.direction
        val map = directionMap.map
        var next = start
        var count = 0L
        while(true) {
           val direction = directions.removeLast()
            directions.addFirst(direction)
            val node = map.get(next)
            count ++
            next = when (direction) {
                'L' -> node!!.first
                else -> node!!.second
            }
            if (next == end) {
                return count
            }
        }
    }

    fun findStepsToEnd(directionMap: MapDirections, starting: List<String>): Long {
        val directions = directionMap.direction
        val map = directionMap.map
        var nextList: MutableList<String> = starting.toMutableList()
        var count = 0L
        while(true) {
            val direction = directions.removeLast()
            directions.addFirst(direction)
            count ++
            var nextNodes: MutableList<String> = mutableListOf()
            for (next in nextList) {
                val node = map.get(next)
                nextNodes += when (direction) {
                    'L' -> node!!.first
                    else -> node!!.second
                }
            }
            nextList = nextNodes
            if (nextList.filter { it.last() == 'Z'}.size == nextList.size) {
                return count
            }
        }
    }

    fun part1(inputs: List<String>): Long {
        val directions = parseMap(inputs)
        val count = findStepsToEnd(directions, "AAA", "ZZZ")
        return count
    }

    fun part2(inputs: List<String>): Long {
        val directions = parseMap(inputs)
        val startingNodes = directions.map.keys.filter { it.last() == 'A' }
        val count = findStepsToEnd(directions, startingNodes)

        return count
    }

    val testInput = readInput("Day08_test")
    println("--- Part 1 test ---")
    val part1Answer = part1(testInput)

    val part1Solution = 2L
    if (part1Answer != part1Solution) {
        error("Part 1 test failed = $part1Answer != $part1Solution")
        return
    } else {
        println("Successful")
    }

    println("--- Part 1 test - 2 ---")
    val testInput1Part2 = readInput("Day08_test1-2")
    val part1Test2Answer = part1(testInput1Part2)

    val part1Part2Solution = 6L
    if (part1Test2Answer != part1Part2Solution) {
        error("Part 1 test - 2 failed = $part1Test2Answer != $part1Part2Solution")
        return
    } else {
        println("Successful")
    }

    println("--- Part 1 ---")
    val input = readInput("Day08")
    part1(input).println()

    println("--- Part 2 test ---")
    val testInput2 = readInput("Day08_test2")
    val part2Answer = part2(testInput2)
    val part2Solution = 6L
    if (part2Answer != part2Solution) {
        error("Part 2 test failed = $part2Answer != $part2Solution")
        return
    } else {
        println("Successful")
    }
    val testInput2Part2 = readInput("Day08_test2-2")
    val solution = part2(testInput2Part2)
    println("Solution $solution")


    println("--- Part 2 ---")
    part2(input).println()
}
