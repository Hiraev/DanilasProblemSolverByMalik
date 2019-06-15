package solver

import extentions.toBooleanArray
import io.Input
import java.util.*


class GeneticSolver(knapsackSize: Int, val initSolution: Solution) : Solver {

    private val MUTATION_PROBABILITY = 0.3
    private val DEFAULT_POPULATION_SIZE = 32

    lateinit var knapsack: List<Input.Item>
    var load: Int = 0
    private val chromosomeSize = knapsackSize

    private var population: List<ChromosomeBool>
    private val random = Random(Random().nextLong())
    private val populations: Int = DEFAULT_POPULATION_SIZE

    init {
        population = when ((populations % 2)) {
            0 -> generateChromosomes(populations)
            else -> generateChromosomes(populations + 1)
        }
    }

    override fun solve(items: List<Input.Item>, account: Int, callback: (Solution) -> Unit) {
        knapsack = items
        load = account
        while (true) {
            population = selection()
            population = population + crossover()
            mutation()
            val bestChromosome = population.filter { !it.fined }.sortedBy { -it.evolution() }.first()
            val solution = mutableListOf<Input.Item>()
            knapsack.forEachIndexed { index, thing -> if (bestChromosome.genes[index]) solution += thing }
            callback.invoke(Solution.from(solution))
        }
    }

    private fun generateChromosomes(number: Int): List<ChromosomeBool> {
        val result = mutableListOf<ChromosomeBool>()
        for (i in 0 until number - 1) result += ChromosomeBool(chromosomeSize, random)
        result += ChromosomeBool(initSolution.ids.toBooleanArray(chromosomeSize))
        return result
    }

    private fun selection() = population.sortedBy { -it.evolution() }.subList(0, population.size / 2)

    private fun crossover(): List<ChromosomeBool> {
        if (population.size % 2 == 1) population = population + population[0]
        val offSprings = mutableListOf<ChromosomeBool>()
        val pop = population.shuffled(random)
        val firstParentsList = pop.subList(0, pop.size / 2)
        val secondParentsList = pop.subList(pop.size / 2, pop.size)
        for (i in 0 until pop.size / 2) {
            val children = firstParentsList[i].crossoverPair(secondParentsList[i])
            offSprings += children.first
            offSprings += children.second
        }
        return offSprings.toList()
    }

    private fun mutation() {
        population = population.sortedBy { -it.evolution() }
        val from = population.size / 2
        val bound = 1 + (chromosomeSize * 0.1).toInt()
        population.forEachIndexed { index, it ->
            if (index > from) {
                for (i in 0..random.nextInt(bound))
                    if (random.nextDouble() <= MUTATION_PROBABILITY) it.mutate(random)
            }
        }
    }

    inner class ChromosomeBool(val genes: BooleanArray) {

        constructor(size: Int, random: Random) :
                this(BooleanArray(size) { (random.nextInt() % 2 == 1) })

        var fined = false
        val size = genes.size

        fun crossover(another: ChromosomeBool): ChromosomeBool {
            assert(another.size == size)
            val genes = mutableListOf<Boolean>()
            for (i in 0 until size) {
                genes += if ((i % 2) == 0) this.genes[i] else another.genes[i]
            }
            return ChromosomeBool(genes.toBooleanArray())
        }

        fun crossoverPair(another: ChromosomeBool) = Pair(this.crossover(another), another.crossover(this))

        fun mutate(random: Random) {
            val index = random.nextInt(size)
            genes[index] = !genes[index]
        }

        fun evolution(): Double {
            var points = 0.0
            var weight = 0.0

            knapsack.forEachIndexed { index, thing ->
                if (genes[index]) {
                    points += thing.value
                    weight += thing.price
                }
            }
            if (weight > load) {
                fined = true
                points -= points * (weight / load.toDouble())
            }
            return points
        }

    }

}
