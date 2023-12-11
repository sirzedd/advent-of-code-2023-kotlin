enum class Direction {
    NORTH, SOUTH, WEST, EAST
}
fun main() {

    var directionMap = mapOf(
        'F' to mapOf(Direction.WEST to Direction.SOUTH, Direction.NORTH to Direction.EAST),
        'J' to mapOf(Direction.EAST to Direction.NORTH, Direction.SOUTH to Direction.WEST),
        '7' to mapOf(Direction.EAST to Direction.SOUTH, Direction.NORTH to Direction.WEST),
        'L' to mapOf(Direction.WEST to Direction.NORTH, Direction.SOUTH to Direction.EAST),
        '-' to mapOf(Direction.WEST to Direction.WEST, Direction.EAST to Direction.EAST),
        '|' to mapOf(Direction.SOUTH to Direction.SOUTH, Direction.NORTH to Direction.NORTH),
    )

    var pathCount: MutableList<MutableList<Int?>> = mutableListOf()
    //row column

    fun findCharPath(inputArray: List<List<Char>>, currentRow: Pair<Int, Int>, goingInDirection: Direction, previousRow: Pair<Int, Int>, awayFromS: Int): Int {
        var row = currentRow.first
        var col = currentRow.second
        var currentRow = currentRow
        var goingInDirection = goingInDirection
        var awayFromS = awayFromS
        if (!(row >= 0 && row < inputArray.size) ||
            !(col >= 0 && col < inputArray[0].size)) {
            //stop from going out of bounds with check
            return 0
        }
        var char = inputArray[row][col]
        if (char == '.') {
            pathCount[row][col] = 0
            return 0
        }
        var nowGoingInDirection = directionMap.get(char)?.get(goingInDirection)
            ?: //Not a valid direction on this tile, but don't need to change pathCount
            return 0

        //this is a good direction, follow it

        while (char != 'S') {
            //determine if the direction we are going on block makes sense
            nowGoingInDirection = directionMap.get(char)?.get(goingInDirection)
                ?: //Not a valid direction on this tile, dead-end
                return 0

            //this is a valid direction, so we've gotten here before if there is a count
            if (pathCount[row][col] != null) {
                //return the value we have now plus the pathCount
                return pathCount[row][col]!! + awayFromS
            }

            //add current pathCount to the list
            pathCount[row][col] = awayFromS

            //no count yet for this tile, add it and move to the tile direction is telling us
            val newCoordinate = when (nowGoingInDirection) {
                Direction.NORTH -> currentRow.first -1 to currentRow.second
                Direction.SOUTH -> currentRow.first + 1 to currentRow.second
                Direction.EAST -> currentRow.first to currentRow.second + 1
                Direction.WEST -> currentRow.first to currentRow.second - 1
            }

            char = inputArray[newCoordinate.first][newCoordinate.second]
            row = newCoordinate.first
            col = newCoordinate.second
            currentRow = newCoordinate
            goingInDirection = nowGoingInDirection
            awayFromS += 1
        }
        return awayFromS
    }


    fun part1(inputs: List<String>): Long {
        val array = inputs.map { it.toCharArray().toList() }
        pathCount = mutableListOf()
        array.forEach{
            println(it)
            pathCount.add(arrayOfNulls<Int>(it.size).toMutableList())
        }
        //find sCoordinate
        val sCoordinateList = inputs.mapIndexed { rowIndex, row -> row.mapIndexed { colIndex, col -> (col == 'S') to (rowIndex to colIndex) }.filter { it.first }}.find { it.size == 1 }!!
        val sCoordinate = sCoordinateList[0].second


        val nextDirections = listOf(
            (sCoordinate.first+1 to sCoordinate.second) to Direction.SOUTH,
            (sCoordinate.first-1 to sCoordinate.second) to Direction.NORTH,
            (sCoordinate.first to sCoordinate.second + 1) to Direction.EAST,
            (sCoordinate.first to sCoordinate.second - 1) to Direction.WEST,
        )
        val sums = nextDirections.map { findCharPath(array, it.first, it.second, sCoordinate, 1) }
        pathCount
            .map { it -> it.map { it ?: 0 }}
            .forEach{
                println(it)
            }
        return sums.max().toLong() / 2

    }

    fun part2(inputs: List<String>): Long {
        return 0
    }

    test( "Part 1 test 1 - 1", "Day10_test1-1", 4L,  ) {i -> part1(i)}
    test( "Part 1 test 1 - 2", "Day10_test1-2", 8L,  ) {i -> part1(i)}

    println("--- Part 1 ---")
    val input = readInput("Day10")
    part1(input).println()

//    test( "Part 2 test", "Day10_test2", 5L,  ) {i -> part2(i)}
//
//    println("--- Part 2 ---")
//    part2(input).println()
}
