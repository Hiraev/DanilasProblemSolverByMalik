package solver

import io.Input

class Solution(
    val price: Int,
    val value: Int,
    val ids: List<Int>
) {
    constructor(item: Input.Item) : this(item.price, item.value, listOf(item.index))

    companion object {
        fun from(items: List<Input.Item>): Solution = items.map(::Solution).reduce { s1, s2 -> s1 + s2 }
    }

    operator fun plus(solution: Solution) =
        Solution(price + solution.price, value + solution.value, ids + solution.ids)
}
