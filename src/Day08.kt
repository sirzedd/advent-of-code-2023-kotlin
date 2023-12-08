import kotlin.math.min

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
            if (next.endsWith(end)) {
                return count
            }
        }
    }

    fun part1(inputs: List<String>): Long {
        val directions = parseMap(inputs)
        val count = findStepsToEnd(directions, "AAA", "ZZZ")
        return count
    }

    fun greatestCommonDivisor(a: Long, b: Long): Long {
        var result = min(a, b)
        while (result > 0) {
            if( a % result == 0L && b % result == 0L) {
                return result
            }
            result --
        }
        return result
    }
    fun leastCommonMultiple(a: Long, b: Long): Long {
        return (a / greatestCommonDivisor(a, b)) * b
    }

    fun part2(inputs: List<String>): Long {
        //I found that using leastCommonMultiple was the best way to solve
        //I'm assuming now that there would have to be a loop, or potentially
        //you'd end up with a new loop and would have a different count after Z
        //especially with different directions. You'd have short and long ones happening at the same time.
        //Had to google how to build leastCommonMultiple
        val directions = parseMap(inputs)
        val total = directions.map.keys.filter { it.last() == 'A' }
            .map { startingPoint ->
                findStepsToEnd(directions, startingPoint, "Z")
            }.reduce{ acc, l -> leastCommonMultiple(acc, l) }

        return total
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

    println("--- Part 2 ---")
    part2(input).println()
}
