package solver

import extentions.withoutLast
import io.Input

class GreedySolver : Solver {

    override fun solve(items: List<Input.Item>, account: Int, callback: (Solution) -> Unit) {

        callback.invoke(fillKnapsackGreedy(account, items))

    }

    private tailrec fun fillKnapsackGreedySorted(
        sum: Int,
        items: List<Input.Item>,
        baseSolution: Solution = Solution(0, 0, emptyList())
    ): Solution {
        if (sum <= 0 || items.isEmpty()) return baseSolution
        val itemsWithoutLast = items.withoutLast()
        val last = items.last()
        return fillKnapsackGreedySorted(
            sum = if (last.price > sum) sum else sum - last.price,
            items = itemsWithoutLast,
            baseSolution = if (last.price > sum) baseSolution else baseSolution + Solution(last)
        )
    }

    private fun fillKnapsackGreedy(load: Int, items: List<Input.Item>): Solution {
        val sorted = items.sortedWith(Comparator { o1, o2 ->
            (o1.value.toDouble() / o1.price).compareTo(o2.value.toDouble() / o2.price)
        })
        return fillKnapsackGreedySorted(load, sorted)
    }

}
