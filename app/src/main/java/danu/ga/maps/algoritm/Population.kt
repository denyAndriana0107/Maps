package unpas.ac.maps.algoritm

import java.util.*

class Population {
    private var populationSize: Int ?= 0
    private var numCity: Int ?= 0
    private var crossOverRate: Double
    private lateinit var child1: Array<City?>
    private lateinit var child2: Array<City?>
    private var mutationRate : Double
    private lateinit var nextGen: Array<Path?>
    private var done: Int
    private var population: Array<Path?>

    constructor(populationSize: Int, numCity: Int, crossOverRate: Double, mutationRate: Double){
        this.populationSize = populationSize
        this.numCity = numCity
        population = Array(populationSize){ Path(numCity) }
        this.crossOverRate = crossOverRate
        this.mutationRate = mutationRate
        this.nextGen = Array(populationSize){Path(numCity)}
        val p = Path(numCity)
        done = 0
        createPopulation(p)

    }

    // Init Population Wisata
    fun createPopulation(p: Path){
        var i = 0
        val waktu = 11
        while (i<populationSize!!){
            val tmpCity = arrayOfNulls<City>(numCity!!)

            for (j in 0 until tmpCity.size) {
                    tmpCity[j] = p.getPath().get(j)
            }
            Collections.shuffle(Arrays.asList(tmpCity));
            val tmpPath = Path(numCity!!)
            tmpPath.setPath(tmpCity)
            population[i] = tmpPath
            i++
        }

    }
    fun ceckTime(p: Path){
        val waktu = 11
        val tmpCity = arrayOfNulls<City>(numCity!!)
        var j = 1
        for (i in 0 until tmpCity.size){
            if (p.getPath().get(i)?.getTutup()!! <= waktu && p.getPath().get(i)?.getBuka()!! >= waktu){
                j++
            }
        }
        print("jumlah:" + j)
    }

    fun getPopulation(): Array<Path?> {
        return population
    }

    fun setPopulation(population: Array<Path?>) {
        this.population = population
    }

    fun getPopulationSize(): Int {
        return populationSize!!
    }

    fun setPopulationSize(populationSize: Int) {
        this.populationSize = populationSize
    }

    //selecting parent
    fun SelectParent(): Int {
        var total = 0
        //memilih parents
        val totalCost: Int = calculateTotalFitness()
        val fit: Int = RandomNum(0, totalCost)
        var value = 0
        for (i in 0 until population.size) {
            value += population[i]!!.getFitness()!!
            if (fit <= value) {
                return i
            }
        }
        return -1
    }
    fun RandomNum(min: Int, max: Int): Int {
        return min + Random().nextInt(max - min)
    }

    //menghitung nilai fitness
    fun FitnessOrder() {
        Arrays.sort(population)
        for (i in 0 until population.size) {
//            val lol: Int = 100 / (population[i]!!.getCost()!!.toInt() + 1)
            val lol: Int = 100 / (population[i]!!.getCost()!!.toInt()+10)
            population[i]!!.setFitness(lol)
        }
    }
    fun calculateTotalFitness(): Int {
        var cost:Int =0
        for (i in 0 until population.size) {
            cost += population[i]!!.getFitness()!!
        }
        return cost
    }

    // Mutasi
     fun Mutation(child: Array<City?>): Array<City?> {
        val check = RandomNum(0, 100)

        if (check <= mutationRate * 100) {
            val point1 = RandomNum(0, numCity!! - 1)
            var point2 = RandomNum(0, numCity!! - 1)
            while (point2 == point1) {
                point2 = RandomNum(0, numCity!! - 1)
            }
            val city1 = child[point1]
            val city2 = child[point2]
            child[point1] = city2
            child[point2] = city1
        }
        return child
    }


    fun AddToGenerationCheckFull(child1: Array<City?>?, child2: Array<City?>?): Boolean {
        if (this.done === this.populationSize) {
            return true
        }
        val newGenChild1 = Path(numCity!!)
        val newGenChild2 = Path(numCity!!)
        newGenChild1.setPath(child1!!)
        newGenChild2.setPath(child2!!)
        return if (done < populationSize!! - 2) {
            this.nextGen[done] = newGenChild1
            this.nextGen[done + 1] = newGenChild2
            this.done += 2
            false
        } else if (done == populationSize!! - 2) {
            this.nextGen[done] = newGenChild1
            this.nextGen[done + 1] = newGenChild2
            this.done += 2
            true
        } else {
            this.nextGen[done] = newGenChild1
            this.done += 1
            true
        }
    }

    fun Mate(): Boolean {
        //Generate a random number to check if the parents cross
        val check = RandomNum(0, 100)
        val parent1 = SelectParent()
        var parent2 = SelectParent()
        while (parent1 == parent2) {
            parent2 = SelectParent()
        }

        //check if there is going to be a crossover
        return if (check <= crossOverRate * 100) {
            val crossoverPoint = RandomNum(0, population[parent1]!!.getPath().size - 1)
            child1 = Array(numCity!!){City(0, 0.0, 0.0, 0, 0)}
            child2 = Array(numCity!!){City(0, 0.0, 0.0, 0, 0)}

            //crossing over
            for (i in 0 until crossoverPoint) {
                child1[i] = population[parent2]!!.getPath()[i]!!
                child2[i] = population[parent1]!!.getPath()[i]!!
            }
            for (i in crossoverPoint until numCity!!) {
                child1[i] = population[parent1]!!.getPath()[i]!!
                child2[i] = population[parent2]!!.getPath()[i]!!
            }


            //Rearrange childs considering city repetition
            var cityChild1: Int
            var cityChild2: Int
            val list1 = ArrayList<Int>()
            val list2 = ArrayList<Int>()
            for (i in 0 until crossoverPoint) {
                cityChild1 = child1[i]!!.getID()!!.toInt()
                cityChild2 = child2[i]!!.getID()!!.toInt()

                //Get the positions of repeated values
                for (j in crossoverPoint until numCity!!) {
                    if (cityChild1 == child1[j]!!.getID()) {
                        list1.add(j)
                    }
                    if (cityChild2 == child2[j]!!.getID()) {
                        list2.add(j)
                    }
                }
            }

            //Find the missing values
            for (i in 0 until numCity!!) {
                var found = false
                //Fixing Child1
                for (j in 0 until numCity!!) {
                    if (population[parent2]!!.getPath()[i] === child1[j]) {
                        found = true
                        break
                    }
                }
                if (found == false) {
                    child1[list1.removeAt(list1.size - 1)] = population[parent2]!!.getPath()[i]
                }
                found = false
                //Fixing Child2
                for (j in 0 until numCity!!) {
                    if (population[parent1]!!.getPath()[i] === child2[j]) {
                        found = true
                        break
                    }
                }
                if (found == false) {
                    child2[list2.removeAt(list2.size - 1)] = population[parent1]!!.getPath()[i]
                }
            }
            if (AddToGenerationCheckFull(child1, child2) == false) {
                false
            } else {
                true
            }
        } else {
            if (AddToGenerationCheckFull(population[parent1]!!.getPath(), population[parent1]!!.getPath()) == false) {
                false
            } else {
                true
            }
        }
    }

    fun getNextGen(): Array<Path?> {
        return nextGen
    }

    fun setNextGen(nextGen: Array<Path?>) {
        this.nextGen = nextGen
    }


    fun getDone(): Int {
        return done!!
    }

    fun setDone(done: Int) {
        this.done = done
    }

}


