import io.Input
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import solver.GeneticSolver
import solver.GreedySolver
import solver.RandomSolver
import solver.Solution
import kotlin.random.Random


class Tests {

    val size = 2000
    val input = Input(
        (0 until size).toList(),
        generateSequence { Random.nextInt(10, 200) }.take(size).toList(),
        generateSequence { Random.nextInt(10, 70) }.take(size).toList(),
        789,
        2
    )

    var randomSolution: Solution = Solution(Int.MAX_VALUE, -100, listOf(0))

    var greedySolution: Solution = Solution(Int.MAX_VALUE, -100, listOf(0))

    var geneticSolution: Solution = Solution(Int.MAX_VALUE, -100, listOf(0))

    @Test
    fun randomSolver() {
        val start = System.currentTimeMillis()
        runBlocking {
            val randomSolverJob = GlobalScope.launch {
                RandomSolver().solve(input.toItems(), input.account, ::setRandomSolutionIfNeeded)
            }
            val greedySolverJob = GlobalScope.launch {
                GreedySolver().solve(input.toItems(), input.account, ::setGreedySolutionIfNeeded)
            }
            val geneticSolverJob = GlobalScope.launch {
                greedySolverJob.join()
                GeneticSolver(input.ids.size, greedySolution).solve(
                    input.toItems(),
                    input.account,
                    ::setGeneticSolutionIfNeeded
                )
            }
            delay(50000)
            randomSolverJob.cancel()
            greedySolverJob.cancel()
            geneticSolverJob.cancel()
        }
        printSolution("Greedy", greedySolution)
        printSolution("Random", randomSolution)
        printSolution("Genetic", geneticSolution)
        println(System.currentTimeMillis() - start)
    }

    private fun setRandomSolutionIfNeeded(solution: Solution) {
        randomSolution = chooseBest(randomSolution, solution)
    }

    private fun setGreedySolutionIfNeeded(solution: Solution) {
        greedySolution = chooseBest(greedySolution, solution)
    }

    private fun setGeneticSolutionIfNeeded(solution: Solution) {
        geneticSolution = chooseBest(geneticSolution, solution)
    }


    private fun chooseBest(solution1: Solution, solution2: Solution): Solution =
        if (solution1.value > solution2.value) {
            solution1
        } else if (solution1.value == solution2.value) {
            if (solution1.price < solution2.price) solution1 else solution2
        } else {
            solution2
        }

    fun printSolution(prefix: String, solution: Solution) {
        println("$prefix price: ${solution.price}, value: ${solution.value}")
    }

}