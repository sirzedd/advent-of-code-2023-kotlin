//My initial attempt
fun main() {
    data class Almanac(
        var seeds: List<Int> = mutableListOf(),
        var seedToSoil: MutableMap<Int, Int> = mutableMapOf(), //source to destination
        var soilToFertilizer: MutableMap<Int, Int> = mutableMapOf(), //source to destination
        var fertilizerToWater: MutableMap<Int, Int> = mutableMapOf(), //source to destination
        var waterToLight: MutableMap<Int, Int> = mutableMapOf(), //source to destination
        var lightToTemperature: MutableMap<Int, Int> = mutableMapOf(), //source to destination
        var temperatureToHumidity: MutableMap<Int, Int> = mutableMapOf(), //source to destination
        var humidityToLocation: MutableMap<Int, Int> = mutableMapOf(), //source to destination

    )

    //map and end index
    fun grabMapUntilReturn(inputs: List<String>, start: Int): Pair<Map<Int, Int>, Int> {
        val map: MutableMap<Int, Int> = mutableMapOf()
        for (i in start .. inputs.size-1) {
            val line = inputs[i]
            if (line.isBlank()) {
                return map to i + 1
            }
            val values = line.split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
            val destination = values[0]
            val source = values[1]
            val range = values[2]

            for (j in 0 .. range - 1) {
                map += source + j to destination + j
            }
        }
        return map to inputs.size
    }

    fun parseAlmanac(inputs: List<String>): Almanac {
        var almanac = Almanac()
        almanac.seeds = inputs.get(0).split(":")[1].split(" ").filter { it.isNotEmpty() }.map { it.toInt() }

        var i = 2
        while(i < inputs.size) {
            var line = inputs[i]
            when {
                line.contains("seed-to-soil map:") -> {
                    val (map, newIndex) = grabMapUntilReturn(inputs, i + 1)
                    almanac.seedToSoil += map
                    i = newIndex
                }
                line.contains("soil-to-fertilizer map:") -> {
                    val (map, newIndex) = grabMapUntilReturn(inputs, i + 1)
                    almanac.soilToFertilizer += map
                    i = newIndex
                }
                line.contains("fertilizer-to-water map:") -> {
                    val (map, newIndex) = grabMapUntilReturn(inputs, i + 1)
                    almanac.fertilizerToWater += map
                    i = newIndex
                }
                line.contains("water-to-light map:") -> {
                    val (map, newIndex) = grabMapUntilReturn(inputs, i + 1)
                    almanac.waterToLight += map
                    i = newIndex
                }
                line.contains("light-to-temperature map:") -> {
                    val (map, newIndex) = grabMapUntilReturn(inputs, i + 1)
                    almanac.lightToTemperature += map
                    i = newIndex
                }
                line.contains("temperature-to-humidity map:") -> {
                    val (map, newIndex) = grabMapUntilReturn(inputs, i + 1)
                    almanac.temperatureToHumidity += map
                    i = newIndex
                }
                line.contains("humidity-to-location map:") -> {
                    val (map, newIndex) = grabMapUntilReturn(inputs, i + 1)
                    almanac.humidityToLocation += map
                    i = newIndex
                }
                else -> {
                    i++
                }
            }
        }

        return almanac

    }

    fun findLowestLocationBySeed(seed: Int, almanac: Almanac): Int {
        val soil = almanac.seedToSoil.getOrDefault(seed, seed)
        val fertilizer = almanac.soilToFertilizer.getOrDefault(soil, soil)
        val water = almanac.fertilizerToWater.getOrDefault(fertilizer, fertilizer)
        val light = almanac.waterToLight.getOrDefault(water, water)
        val temperature = almanac.lightToTemperature.getOrDefault(light, light)
        val humidity = almanac.temperatureToHumidity.getOrDefault(temperature, temperature)
        val location = almanac.humidityToLocation.getOrDefault(humidity, humidity)
        return location
    }

    fun findLowestValue(almanac: Almanac): Int{
        val seeds = almanac.seeds
        return seeds.minOf { findLowestLocationBySeed(it, almanac) }
    }

    fun part1(inputs: List<String>): Int {
        val almanac = parseAlmanac(inputs)
        return findLowestValue(almanac)
    }

    fun part2(inputs: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day05_test")
    val part1Answer = part1(testInput)
    check(part1Answer == 35 )

    val input = readInput("Day05")
    part1(input).println()
//
//    val testInput2 = readInput("Day05_test2")
//    val part2Answer = part2(testInput2)
//    check(part2Answer == 30)
//    part2(input).println()
}
