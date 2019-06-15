package io

data class Input(
    val ids: List<Int>,
    val prices: List<Int>,
    val values: List<Int>,
    val account: Int,
    val max_time: Long
) {

    fun getTriples(): List<Triple<Int, Int, Int>> =
        ids.zip(prices).zip(values) { a, b -> Triple(a.first, a.second, b) }

    fun toItems() = getTriples().map { Item(it.first, it.second, it.third) }

    fun toShuffledItems() = toItems().shuffled()

    inner class Item(
        val index: Int,
        val price: Int,
        val value: Int
    )

}
