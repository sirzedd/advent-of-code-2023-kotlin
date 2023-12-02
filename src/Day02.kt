import kotlin.io.path.Path
//My initial attempt
fun main() {

    data class Cubes(
        var red: Int = 0,
        var green: Int = 0,
        var blue: Int = 0
    )

    data class Game(
        val game: Int,
        val sets: List<Cubes>
    )

    fun getCubesForSet(set: String): Cubes {
        //6 red, 1 blue, 3 green
        val cubeList =  set.split(",").map { it.trim() }
        val cubes = Cubes()
        for (str in cubeList) {
            val valColor = str.split(" ")
            when (valColor[1]) {
                "red" -> cubes.red = valColor[0].toInt()
                "blue" -> cubes.blue = valColor[0].toInt()
                "green" -> cubes.green = valColor[0].toInt()
                else -> {}
            }
        }

        return cubes;
    }

    fun parseGame(input: String): Game {
        val gameSets = input.split(":")
        val game = gameSets[0].split(" ")[1].toInt()

        val sets = gameSets[1].split(";")
            .map { getCubesForSet(it) }
        return Game(game, sets)
    }


    fun getGameIfPossible(input: String, red: Int, green: Int, blue: Int): Int {
        val game = parseGame(input)

        //for set of cubes make sure it falls in the maximum
        for (cubes in game.sets) {
            if(cubes.red > red || cubes.blue > blue || cubes.green > green) {
                return 0
            }
        }
        return game.game
    }
    fun part1(inputs: List<String>): Int =
        //12 red cubes, 13 green cubes, and 14 blue cubes
        inputs.sumOf {  getGameIfPossible(it,12, 13, 14 ) }


    fun getPowerOfCube(input: String): Int {
        val game = parseGame(input)
        val largestCubes = Cubes()
        //for each set of cubes, build the largest
        for (cubes in game.sets) {
            //Set largest values only
            if(largestCubes.red < cubes.red){
                largestCubes.red = cubes.red
            }
            if(largestCubes.blue < cubes.blue){
                largestCubes.blue = cubes.blue
            }
            if(largestCubes.green < cubes.green){
                largestCubes.green = cubes.green
            }
        }

        //power it
        val power = largestCubes.red * largestCubes.blue * largestCubes.green
        return power
    }

    fun part2(inputs: List<String>): Int =
        inputs.sumOf {  getPowerOfCube(it) }

    val testInput = readInput("Day02_test")
    val part1Answer = part1(testInput)
    check(part1Answer == 8)

    val input = readInput("Day02")
    part1(input).println()

    val testInput2 = readInput("Day02_test2")
    val part2Answer = part2(testInput2)
    check(part2Answer == 2286)
    part2(input).println()
}
