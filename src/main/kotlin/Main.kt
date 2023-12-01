import java.util.*
import java.io.File

const val year = 2023
fun main(args: Array<String>) {
    // If there are command line arguments, run the specified days, all days, or the highest implemented day.
    if (args.isNotEmpty()) {
        when (args[0]) {
            "a" -> {
                for (day in 1..25) {
                    runDay(day)
                }
            }
            "r" -> runLatest()
            else -> runDay(args[0].toInt())
        }
        return
    }

    // If there aren't command line arguments, prompt the user for input.
    val scanner = Scanner(System.`in`)
    print("Pick a day, \"a\" for all, or \"r\" for highest implemented: ")
    val input = scanner.nextLine()
    if (input == "a") {
        val startTime = System.currentTimeMillis()
        for (day in 1..25) {
            runDay(day)
        }
        val endTime = System.currentTimeMillis()
        println("Total time: ${(endTime - startTime) / 1000.0} seconds")
    } else if (input == "r" || input == "") {
        runLatest()
    } else if (input.toInt() in 1..25) {
        runDay(input.toInt())
    }
}

/**
 * Run a specific day.
 */
fun runDay(day: Int) {
    try {
        val dayString = day.toString().padStart(2, '0')
        val dayClass = Class.forName("adventOfCode$year.Day$dayString")
        val dayConstructor = dayClass.getConstructor(String::class.java)

        if (File("src/main/resources/Day${dayString}test.txt").exists()) {
            val dayData = File("src/main/resources/Day${dayString}test.txt").readText()
            val dayInstance = dayConstructor.newInstance(dayData)
            println("Day $day (test)")
            dayInstance.javaClass.getMethod("run").invoke(dayInstance)
        }
        val dayData = File("src/main/resources/Day${dayString}.txt").readText()
        val dayInstance = dayConstructor.newInstance(dayData)
        println("Day $day")
        dayInstance.javaClass.getMethod("run").invoke(dayInstance)
    } catch (e: ClassNotFoundException) {
        println("Day $day\n\tNot Implemented")
    }
}

/**
 * Run the highest implemented day.
 */
fun runLatest() {
    // would love to know if there's a better way to do this
    for (day in 2..26) {
        try {
            val dayString = day.toString().padStart(2, '0')
            Class.forName("adventOfCode$year.Day$dayString")
        } catch (e: ClassNotFoundException) {
            runDay(day - 1)
            break
        }
    }
}
