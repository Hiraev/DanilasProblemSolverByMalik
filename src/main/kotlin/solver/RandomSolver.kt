package solver

import extentions.indecies
import io.Input
import java.util.*
import kotlin.random.Random


class RandomSolver : Solver {

    override fun solve(items: List<Input.Item>, account: Int, callback: (Solution) -> Unit) {
        while (true) {
            val bitField = BitSet(items.size)
            var sum = 0
            var value = 0
            items.shuffled().withIndex().map { (i, item) ->
                if (Random.nextBoolean() && sum + item.price <= account) {
                    bitField.set(i)
                    sum += item.price
                    value += item.value
                }
            }
            callback.invoke(Solution(sum, value, bitField.indecies()))
        }
    }

}
