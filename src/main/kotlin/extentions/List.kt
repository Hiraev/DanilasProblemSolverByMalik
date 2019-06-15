package extentions

fun <T> List<T>.withoutLast() = dropLast(1)

fun List<Int>.toBooleanArray(size: Int): BooleanArray {
    val booleanArray = BooleanArray(size)
    forEach {
        booleanArray[it] = true
    }
    return booleanArray
}
