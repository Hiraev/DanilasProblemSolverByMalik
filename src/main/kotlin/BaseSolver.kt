import io.Output
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import solver.GeneticSolver
import solver.GreedySolver
import solver.RandomSolver
import solver.Solution
import utils.parse
import utils.readInputJson
import utils.writeOutputInFile

var randomSolution: Solution = Solution(Int.MAX_VALUE, -100, listOf(0))

var greedySolution: Solution = Solution(Int.MAX_VALUE, -100, listOf(0))

var geneticSolution: Solution = Solution(Int.MAX_VALUE, -100, listOf(0))

fun main(args: Array<String>) {

    val ioFiles = parse(args)
    val input = readInputJson(ioFiles.input)

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

        delay(input.max_time * 1000)

        randomSolverJob.cancel()
        greedySolverJob.cancel()
        geneticSolverJob.cancel()
        val bestSolution = chooseBest(chooseBest(randomSolution, greedySolution), geneticSolution)
        writeOutputInFile(ioFiles.output, Output(bestSolution.ids))
    }
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
