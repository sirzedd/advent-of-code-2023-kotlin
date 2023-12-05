//My initial attempt
fun main() {

    data class SourceAndDestination(
        var source: LongRange,
        var destination: LongRange,
    )
    data class Almanac(
        var seeds: List<Long> = mutableListOf(),
        var seedToSoil: List<SourceAndDestination> = mutableListOf(),
        var soilToFertilizer: List<SourceAndDestination> = mutableListOf(),
        var fertilizerToWater: List<SourceAndDestination> = mutableListOf(),
        var waterToLight: List<SourceAndDestination> = mutableListOf(),
        var lightToTemperature: List<SourceAndDestination> = mutableListOf(),
        var temperatureToHumidity: List<SourceAndDestination> = mutableListOf(),
        var humidityToLocation: List<SourceAndDestination> = mutableListOf(),
    )

    //Build List of Source Destinations
    fun buildSourceDestinationsUntilBlank(inputs: List<String>, start: Int): Pair<List<SourceAndDestination>, Int> {
        var ranges: List<SourceAndDestination> = mutableListOf()
        for (i in start .. inputs.size-1) {
            val line = inputs[i]
            if (line.isBlank()) {
                return ranges to i + 1
            }
            val values = line.split(" ").filter { it.isNotEmpty() }.map { it.toLong() }
            val destination = values[0]
            val source = values[1]
            val range = values[2]

            val sourceAndDestination = SourceAndDestination(
                source = source .. source + range,
                destination = destination .. destination + range,
            )
            ranges += sourceAndDestination

        }
        return ranges to inputs.size
    }

    //Parse the almanac
    fun parseAlmanac(inputs: List<String>): Almanac {
        var almanac = Almanac()
        almanac.seeds = inputs.get(0).split(":")[1].split(" ").filter { it.isNotEmpty() }.map { it.toLong() }

        var i = 2
        while(i < inputs.size) {
            var line = inputs[i]
            when {
                line.contains("seed-to-soil map:") -> {
                    val (map, newIndex) = buildSourceDestinationsUntilBlank(inputs, i + 1)
                    almanac.seedToSoil += map
                    i = newIndex
                }
                line.contains("soil-to-fertilizer map:") -> {
                    val (map, newIndex) = buildSourceDestinationsUntilBlank(inputs, i + 1)
                    almanac.soilToFertilizer += map
                    i = newIndex
                }
                line.contains("fertilizer-to-water map:") -> {
                    val (map, newIndex) = buildSourceDestinationsUntilBlank(inputs, i + 1)
                    almanac.fertilizerToWater += map
                    i = newIndex
                }
                line.contains("water-to-light map:") -> {
                    val (map, newIndex) = buildSourceDestinationsUntilBlank(inputs, i + 1)
                    almanac.waterToLight += map
                    i = newIndex
                }
                line.contains("light-to-temperature map:") -> {
                    val (map, newIndex) = buildSourceDestinationsUntilBlank(inputs, i + 1)
                    almanac.lightToTemperature += map
                    i = newIndex
                }
                line.contains("temperature-to-humidity map:") -> {
                    val (map, newIndex) = buildSourceDestinationsUntilBlank(inputs, i + 1)
                    almanac.temperatureToHumidity += map
                    i = newIndex
                }
                line.contains("humidity-to-location map:") -> {
                    val (map, newIndex) = buildSourceDestinationsUntilBlank(inputs, i + 1)
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

    fun findNextValueInRanges(value: Long, sourceAndDestinations: List<SourceAndDestination>): Long {
        //find the range that value is in
        val newValue = sourceAndDestinations.find { it.source.contains(value) }
            ?.let {
                //find offset from source
                val offset = value - it.source.first
                //find destination based on offset so we don't store them all
                val nextValue = it.destination.first + offset
                return nextValue
            } ?: value
        return newValue
    }

    fun findLowestLocationBySeed(seed: Long, almanac: Almanac): Long {
        val soil = findNextValueInRanges(seed, almanac.seedToSoil)
        val fertilizer = findNextValueInRanges(soil, almanac.soilToFertilizer)
        val water = findNextValueInRanges(fertilizer, almanac.fertilizerToWater)
        val light = findNextValueInRanges(water, almanac.waterToLight)
        val temperature = findNextValueInRanges(light, almanac.lightToTemperature)
        val humidity = findNextValueInRanges(temperature, almanac.temperatureToHumidity)
        val location = findNextValueInRanges(humidity, almanac.humidityToLocation)
        return location
    }

    fun part1(inputs: List<String>): Long {
        val almanac = parseAlmanac(inputs)
        return almanac.seeds.minOf{ findLowestLocationBySeed(it, almanac) }
    }

    fun findLowestValuePart2(almanac: Almanac): Long{
        val seedRange = almanac.seeds.chunked(2).map { it[0] .. it[0] + it[1] }
        //get the minimum of the ranges, step through each range and take the min
        return seedRange.minOf { it.step(1).minOf { findLowestLocationBySeed(it, almanac) }}
    }

    fun part2(inputs: List<String>): Long {
        val almanac = parseAlmanac(inputs)
        return findLowestValuePart2(almanac)
    }

    val testInput = readInput("Day05_test")
    val part1Answer = part1(testInput)
    check(part1Answer == 35L )

    val input = readInput("Day05")
    part1(input).println()

    val testInput2 = readInput("Day05_test")
    val part2Answer = part2(testInput2)
    check(part2Answer == 46L)
    part2(input).println()
}
