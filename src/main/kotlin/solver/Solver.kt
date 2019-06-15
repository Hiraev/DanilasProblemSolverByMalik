package solver

import io.Input

interface Solver {
    fun solve(items: List<Input.Item>, account: Int, callback: (Solution) -> Unit)
}
