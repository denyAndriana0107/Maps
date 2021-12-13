package danu.ga.maps.algoritm

import unpas.ac.maps.algoritm.City
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Test {
    var pathArray:Array<City> = Array(4){ City(0, 0.0, 0.0,0,0) }
}
fun main(){
    val currentTimestamp = System.currentTimeMillis()
    val current = LocalDateTime.now()

    var t:Test = Test()
    t.pathArray[0] = City(1,-6.837117742309791,108.23474243750054,8,15)
    t.pathArray[1] = City(2,-6.833511818974356,108.23374125613613,7,22)
    t.pathArray[2] = City(3,-6.832790326613714,108.21439662980889,7,18)
    t.pathArray[3] = City(4,-6.835272384200842,108.22775403108928,7,20)

    val formatter = DateTimeFormatter.ofPattern("HH.mm")
    val formatted = current.format(formatter)
    val time:Double = formatted.toDouble()

    var jumlah:Int = 0
    val list:MutableList<City> = mutableListOf()
    for (i in 0 until t.pathArray.size){
        if (t.pathArray[i].tutup!! > time && t.pathArray[i].buka!! < time){
            list.add(City(t.pathArray[i].id!!,t.pathArray[i].lat!!,t.pathArray[i].lng!!
            ,t.pathArray[i].buka!!,t.pathArray[i].tutup!!))
            jumlah ++
        }
    }
    var arraySelected:Array<City> = Array(jumlah){ City(0, 0.0, 0.0,0,0) }
     arraySelected = list.toTypedArray()

    print("${arraySelected.size}")
}

