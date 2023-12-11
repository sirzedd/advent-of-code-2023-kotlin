enum class Direction {
    NORTH, SOUTH, WEST, EAST
}
fun main() {
    //at 0,0, going WEST 2 to S
//    var visited = mutableMapOf<Pair<Pair<Int, Int>, Direction>, Int>()

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
//    fun findCharPath(inputArray: List<List<Char>>, currentRow: Pair<Int, Int>, goingInDirection: Direction, previousRow: Pair<Int, Int>, awayFromS: Int): Int {
//        val row = currentRow.first
//        val col = currentRow.second
//        println("Checking ($row, $col)")
//        if (!(row >= 0 && row < inputArray.size) ||
//            !(col >= 0 && col < inputArray[0].size)) {
//            println("Out of limits")
//            return 0
//        }
//
//
////        val found = visited.get((row to col) to goingInDirection)
////        if (found != null) {
////            println("Already found value $found")
////            return found
////        }
//
//        val currentChar = inputArray[row][col]
//        if (currentChar == '.') {
//            println("($row, $col) == . return 0")
////            visited.put((row to col) to goingInDirection, 0)
//            return 0
//        }
//        if (currentChar == 'S') {
//            println("Found S")
//            return 1
//        }
//        val nowGoingInDirection =  directionMap.get(currentChar)?.get(goingInDirection)
//        if (nowGoingInDirection == null) {
//            println("Not a valid direction change $goingInDirection into $currentChar ")
//            return 0
//        }
//
//        println("checking ($row, $col) coming from into row facing $goingInDirection, will change into $nowGoingInDirection")
//
//        //TODO: The idea is add the number of the current node if it was successful in coming this way
//        // this succeeded so if I was to go the opposite way I wouldn't have to calculate how much to S
//        //it would be in the map.  Heading to (0,1) EAST, EAST works, so (0,1) WEST works, awayFromS + 1
////        val goingBack =  directionMap.get(currentChar)?.get(backwardsMap.get(nowGoingInDirection))!!
////        visited.put((row to col) to goingBack, awayFromS + 1)
//
//        //go in all directions except where I came from
//        val nextDirections = listOf(
//            currentRow.first+1 to currentRow.second,
//            currentRow.first-1 to currentRow.second,
//            currentRow.first to currentRow.second + 1,
//            currentRow.first to currentRow.second - 1,
//        ).filter { it != previousRow }
//        println("Checking next $nextDirections")
//        val foundS = nextDirections.map { findCharPath(inputArray, it, nowGoingInDirection, currentRow, awayFromS + 1)}
////        for (next in nextDirections) {
////            val value = findCharPath(inputArray, next, nowGoingInDirection, currentRow, awayFromS + 1)
////            if (value > 0) {
//////                visited.put((row to col) to goingInDirection, value)
////                return value + 1
////            }
////        }
////        visited.put((row to col) to goingInDirection, 0)
//        return foundS.max()
//
//    }

    fun findCharPath(inputArray: List<List<Char>>, currentRow: Pair<Int, Int>, goingInDirection: Direction, previousRow: Pair<Int, Int>, awayFromS: Int): Int {
        val row = currentRow.first
        val col = currentRow.second
        if (!(row >= 0 && row < inputArray.size) ||
            !(col >= 0 && col < inputArray[0].size)) {
            //stop from going out of bounds with check
            return 0
        }

        //Determine if current direction and tile makes sense
        val char = inputArray[row][col]
        if (char == '.') {
            pathCount[row][col] = 0
            return 0
        }

        if (char == 'S') {
            return awayFromS
        }

        //determine if the direction we are going on block makes sense
        val nowGoingInDirection = directionMap.get(char)?.get(goingInDirection)
            ?: //Not a valid direction on this tile, but don't need to change pathCount
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

        val value = findCharPath(inputArray, newCoordinate, nowGoingInDirection, currentRow, awayFromS + 1)
        return value
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

        //TODO, what about building a list of paths so I can make sure it's going the right way

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
