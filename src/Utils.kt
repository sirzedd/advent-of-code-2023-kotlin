import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) =
    Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

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
    println("--- $partName end---")
}