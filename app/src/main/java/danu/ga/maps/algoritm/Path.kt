package unpas.ac.maps.algoritm

import androidx.lifecycle.ViewModelProvider
import danu.ga.maps.MainActivity
import danu.ga.maps.fragment.MapsFragment
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import unpas.ac.id.maps.network.model.KordinatResponse
import unpas.ac.id.maps.viewModel.WisataViewModel
import unpas.ac.maps.network.NetworkModule
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Path:Comparable<Path>  {
    private lateinit var path:Array<City?>
    private var numCities:Int ?= 0
    private var fitness:Int ?= 0
    private var cost = 0.0


    constructor(numcities: Int){
        //viewModel database
        this.numCities = numcities
        createPath()
        cost = 0.0
        calculateCost()
        fitness = 0
    }

    //menghitung harga bayar
    fun calculateCost() {
        this.cost = 0.0
        var i = 0
        while (i < numCities!! - 1) {
            this.cost += path[i]!!.distance(path[i + 1]?.getLat()!!.toDouble(), path[i + 1]?.getLng()!!.toDouble())
            i++
        }
        this.cost += path[path.size - 1]!!.distance(path[0]?.getLat()!!.toDouble(), path[0]?.getLng()!!.toDouble())
    }

    //Init Path Wisata
    fun createPath(){
       this.path= Array(numCities!!){ City(1, 0.0, 0.0,0,0) }

        //data
        var path2 :Array<City> = Array(14){City(1, 0.0, 0.0,0,0)}
//        path2[0] = City(1,-6.837117742309791,108.23474243750054,8,15)
//        path2[1] = City(2,-6.833511818974356,108.23374125613613,7,22)
//        path2[2] = City(3,-6.832790326613714,108.21439662980889,7,18)
//        path2[3] = City(4,-6.835272384200842,108.22775403108928,7,20)


        //data baru
        path2[0]= City(1 , -7.141948728625862, 107.42001699715476, 7,17)
        path2[1]= City(2, -7.13786837250741, 107.4003322683194, 7, 17)
        path2[2]= City(3, -7.147259667366287, 107.43302599715466, 7, 17)
        path2[3]= City(4, -7.140309854149551, 107.40050826831946, 7, 17)
        path2[4]= City(5, -7.134110954021378, 107.42146499715453, 8, 21)
        path2[5]= City(6, -7.1263228460283345, 107.4213933529785, 8, 21)
        path2[6]= City(7, -7.131829661631447, 107.42214335297857, 8, 21)
        path2[7]= City(8, -7.134542142316982, 107.42175399715457, 8, 21)
        path2[8]= City(9, -7.162362963217781, 107.43179799715483, 9, 17)
        path2[9]= City(10, -7.158514509943944, 107.41803662414344, 9, 21)
        path2[10]= City (11, -7.133991370886841, 107.42228635297856, 9, 17)
        path2[11]= City(12, -7.127602305985058, 107.43041099715458, 10, 21)
        path2[12]= City(13, -7.127168138379776, 107.43032926831937, 10, 21)
        path2[13]= City( 14, -7.130162723424981, 107.43236826831934, 10, 21)


        val currentTimestamp = System.currentTimeMillis()
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH.mm")
        val formatted = current.format(formatter)
        val time:Double = 7.1

        //memasukan ke array utama
        var list:MutableList<City> = mutableListOf()
        var i = 0
        while (i < path2.size ){
                if (path2[i].tutup!! > time && path2[i].buka!! < time){
                    list.add(City(path2[i].id!!,path2[i].lat!!,path2[i].lng!!,path2[i].buka!!,path2[i].tutup!!))
                }
            i++
        }
        path = list.toTypedArray()

//        val d:MapsFragment = MapsFragment()
//        this.path = d.data()
    }
    fun RandomNum(min: Int, max: Int): Int {
        return min + Random().nextInt(max - min)
    }
    fun RandomTime(min: Int, max: Int):Int{
        return  min + Random().nextInt(max - min)
    }

    fun getPath(): Array<City?> {
        return this.path
    }

    fun setPath(path: Array<City?>) {
        this.path = path
        calculateCost()
    }


    fun getCost(): Double? {
        return cost
    }

    fun setCost(distance: Double) {
        this.cost = distance
    }


    //Fitness
    fun getFitness(): Int? {
        return fitness
    }

    fun setFitness(fitness: Int) {
        this.fitness = fitness
    }

    override fun compareTo(obj: Path): Int {
        val tmp = obj
        return if (cost!! < tmp.cost!!) {
            1
        } else if (cost!! > tmp.cost!!) {
            -1
        } else {
            0
        }
    }


}