enum class Direction {
    NORTH, SOUTH, WEST, EAST
}
fun main() {
    //at 0,0, going WEST 2 to S
    var visited = mutableMapOf<Pair<Pair<Int, Int>, Direction>, Int>()

    var directionMap = mapOf(
        'F' to mapOf(Direction.WEST to Direction.SOUTH, Direction.NORTH to Direction.EAST),
        'J' to mapOf(Direction.EAST to Direction.NORTH, Direction.SOUTH to Direction.WEST),
        '7' to mapOf(Direction.EAST to Direction.SOUTH, Direction.NORTH to Direction.WEST),
        'L' to mapOf(Direction.WEST to Direction.NORTH, Direction.SOUTH to Direction.EAST),
        '-' to mapOf(Direction.WEST to Direction.WEST, Direction.EAST to Direction.EAST),
        '|' to mapOf(Direction.SOUTH to Direction.SOUTH, Direction.NORTH to Direction.NORTH),
    )

    var backwardsMap = mapOf(
        Direction.EAST to Direction.WEST,
        Direction.WEST to Direction.EAST,
        Direction.SOUTH to Direction.NORTH,
        Direction.NORTH to Direction.SOUTH
    )


    //row column
    fun findCharPath(inputArray: List<List<Char>>, currentRow: Pair<Int, Int>, goingInDirection: Direction, previousRow: Pair<Int, Int>, awayFromS: Int): Int {
        val row = currentRow.first
        val col = currentRow.second
        println("Checking ($row, $col)")
        if (!(row >= 0 && row < inputArray.size) ||
            !(col >= 0 && col < inputArray[0].size)) {
            println("Out of limits")
            return 0
        }


        val found = visited.get((row to col) to goingInDirection)
        if (found != null) {
            println("Already found value $found")
            return found
        }

        val currentChar = inputArray[row][col]
        if (currentChar == '.') {
            println("($row, $col) == . return 0")
            visited.put((row to col) to goingInDirection, 0)
            return 0
        }
        if (currentChar == 'S') {
            println("Found S")
            return 1
        }
        val nowGoingInDirection =  directionMap.get(currentChar)?.get(goingInDirection)
        if (nowGoingInDirection == null) {
            println("Not a valid direction change $goingInDirection into $currentChar ")
            return 0
        }

        println("checking ($row, $col) coming from into row facing $goingInDirection, will change into $nowGoingInDirection")

        val goingBack =  directionMap.get(currentChar)?.get(backwardsMap.get(nowGoingInDirection))!!
        visited.put((row to col) to goingBack, awayFromS + 1)

        //go in all directions except where I came from
        val nextDirections = listOf(
            currentRow.first+1 to currentRow.second,
            currentRow.first-1 to currentRow.second,
            currentRow.first to currentRow.second + 1,
            currentRow.first to currentRow.second - 1,
        ).filter { it != previousRow }
        println("Checking next $nextDirections")
        for (next in nextDirections) {
            val value = findCharPath(inputArray, next, nowGoingInDirection, currentRow, awayFromS + 1)
            if (value > 0) {
                visited.put((row to col) to goingInDirection, value)
                return value + 1
            }
        }
        visited.put((row to col) to goingInDirection, 0)
        return 0

    }

    fun part1(inputs: List<String>): Long {
        val array = inputs.map { it.toCharArray().toList() }
        array.forEach{ println(it) }
        //find sCoordinate
        val sCoordinateList = inputs.mapIndexed { rowIndex, row -> row.mapIndexed { colIndex, col -> (col == 'S') to (rowIndex to colIndex) }.filter { it.first }}.find { it.size == 1 }!!
        val sCoordinate = sCoordinateList[0].second

        val nextDirections = listOf(
            (sCoordinate.first+1 to sCoordinate.second) to Direction.SOUTH,
            (sCoordinate.first-1 to sCoordinate.second) to Direction.NORTH,
            (sCoordinate.first to sCoordinate.second + 1) to Direction.EAST,
            (sCoordinate.first to sCoordinate.second - 1) to Direction.WEST,
        )
        val sums = nextDirections.map { findCharPath(array, it.first, it.second, sCoordinate, 0) }

        return sums.max().toLong() / 2

    }

    fun part2(inputs: List<String>): Long {
        return 0
    }

//    test( "Part 1 test 1 - 1", "Day10_test1-1", 4L,  ) {i -> part1(i)}
    test( "Part 1 test 1 - 2", "Day10_test1-2", 8L,  ) {i -> part1(i)}

    println("--- Part 1 ---")
    val input = readInput("Day10")
    part1(input).println()

//    test( "Part 2 test", "Day10_test2", 5L,  ) {i -> part2(i)}
//
//    println("--- Part 2 ---")
//    part2(input).println()
}
