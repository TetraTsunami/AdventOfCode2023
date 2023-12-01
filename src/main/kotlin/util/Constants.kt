package util

@Suppress("unused")
object Constants {
    val lowercase = listOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
        'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
        'u', 'v', 'w', 'x', 'y', 'z')
    val uppercase = lowercase.map { it.uppercaseChar() }
    val digits = listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    val hexDigits = digits + listOf('a', 'b', 'c', 'd', 'e', 'f')
}
