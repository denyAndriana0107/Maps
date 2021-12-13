package unpas.ac.maps.algoritm

class Permutation {
    private var cost = 999999
    private lateinit var best: ArrayList<City?>

    fun permutations(list: ArrayList<City?>?) {
        permutations(null, list!!, null)
    }

    fun permutations(prefix: ArrayList<City?>?, suffix: ArrayList<City?>, output: ArrayList<ArrayList<City?>?>?): ArrayList<City?>? {
        var prefix = prefix
        var output = output
        if (prefix == null) prefix = ArrayList()
        if (output == null) output = ArrayList()
        if (suffix.size === 1) {
            var newElement = ArrayList(prefix)
            newElement.addAll(suffix)
            var costNow = cost(newElement).toInt()
            if (costNow < cost) {
                best = newElement
                cost = costNow
            }
            return best
        }
        for (i in 0 until suffix.size) {
            val newPrefix = ArrayList(prefix)
            newPrefix.add(suffix[i])
            val newSuffix = ArrayList(suffix)
            newSuffix.removeAt(i)
            permutations(newPrefix, newSuffix, output)
        }
        return best
    }

    fun getCost(): Int {
        return cost
    }

    fun setCost(cost: Int) {
        this.cost = cost
    }

    fun getBest(): ArrayList<City?>? {
        return best
    }

    fun setBest(best: ArrayList<City?>?) {
        this.best = best!!
    }

    fun cost(path: ArrayList<City?>): Double {
        var cost:Double = 0.0
        var i = 0
        while (i < path.size - 1) {
            cost += path[i]!!.distance(path[i + 1]!!.getLat()!!.toDouble(), path[i + 1]!!.getLng()!!.toDouble())
            println(path[i]!!.distance(path[i + 1]!!.getLat()!!.toDouble(), path[i + 1]!!.getLng()!!.toDouble()))
            i++
        }
        cost += path[path.size - 1]!!.distance(path[0]!!.getLat()!!.toDouble(), path[0]!!.getLng()!!.toDouble())
        return cost
    }
}