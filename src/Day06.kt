import kotlinx.coroutines.runBlocking

//My initial attempt
fun main() {

    data class RaceData(
        val time: Long = 0,
        val distance: Long = 0,
    )

    fun parseRaceData(inputs: List<String>): List<RaceData> {
        val (_, timesString) = inputs[0].split(":")
        val (_, distancesString) = inputs[1].split(":")
        val times = timesString.trim().split(" ").filter { it.isNotBlank()}
        val distance = distancesString.trim().split(" ").filter { it.isNotBlank()}
        val races = times.mapIndexed { index, time -> RaceData(time.toLong(), distance[index].toLong()) }
        return races
    }
    fun parseRaceDataAsSingle(inputs: List<String>): RaceData {
        val (_, timesString) = inputs[0].split(":")
        val (_, distancesString) = inputs[1].split(":")
        val times = timesString.trim().split(" ").filter { it.isNotBlank()}.map { it.trim() }.joinToString("")
        val distance = distancesString.trim().split(" ").filter { it.isNotBlank()}.map { it.trim() }.joinToString("")
        return RaceData(times.toLong(), distance.toLong())
    }

    //smaller code but longer running
    //just go through all of them and see which are larger, count those that are
    fun findNumberOfWaysToBeatDistance2(race: RaceData): Long {
        val time = race.time
        val distance = race.distance

        val count = (0 .. time).step(1).map { (time - it) * it }
            .filter { it > distance}
            .count().toLong()
        return count
    }

    //Faster since we start in the middle and work our way out
    fun findNumberOfWaysToBeatDistance(race: RaceData): Long {
        val time = race.time
        val distance = race.distance

        //Pick a value in the middle, lo, hi and go left and right from middle
        val mid = time / 2
        var lo = mid - 1
        var hi = mid

        var runningTotal = 0L

        //work way down from mid until we find first that doesn't beat
        while (lo > 0) {
            val leftOverTime = time - lo
            val myDistance = leftOverTime * lo
            if (myDistance > distance) {
                runningTotal += 1
                lo --
            } else break;
        }

        //work way up from hi until we find first that doesn't beat
        while (hi < time) {
            val leftOverTime = time - hi
            val myDistance = leftOverTime * hi
            if (myDistance > distance) {
                runningTotal += 1
                hi ++
            } else break;
        }

        return runningTotal
    }

    fun part1(inputs: List<String>): Long {
        val raceDatas = parseRaceData(inputs)
        return raceDatas.foldRight(1L) {raceData, acc -> findNumberOfWaysToBeatDistance2(raceData) * acc  }
    }
    fun part2(inputs: List<String>): Long {
        val raceData = parseRaceDataAsSingle(inputs)
        return findNumberOfWaysToBeatDistance2(raceData)
    }

    val testInput = readInput("Day06_test")
    val part1Answer = part1(testInput)
    val part1Solution = 288L
    if (part1Answer != 288L) {
        error("Part 1 test failed = $part1Answer != $part1Solution")
        return
    }

    val input = readInput("Day06")
    part1(input).println()

    val testInput2 = readInput("Day06_test")
    val part2Answer = part2(testInput2)
    check(part2Answer == 71503L)
    part2(input).println()
}
