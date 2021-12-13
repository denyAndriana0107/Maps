package unpas.ac.maps.algoritm

class City {
    var id:Int ?= 0
    var lat:Double ?= 0.0
    var lng:Double ?= 0.0
    var buka:Int ?= 0
    var tutup:Int ?= 0

    constructor(id: Int, lat: Double, lng: Double, buka: Int, tutup: Int){
        this.id = id
        this.lat = lat
        this.lng = lng
        this.buka = buka
        this.tutup = tutup
    }

    //Setter
    fun setId(id: Int){
       this.id = id
    }
    fun setLat(lat: Double){
        this.lat = lat
    }
    fun setLng(lng: Double){
        this.lng = lng
    }
    fun setBuka(buka: Int){
        this.buka = buka
    }
    fun setTutup(tutup: Int){
        this.tutup = tutup
    }

    //Gettter
    fun getID(): Int? {
        return id
    }
    @JvmName("getLat1")
    fun getLat(): Double? {
        return lat
    }
    @JvmName("getLng1")
    fun getLng(): Double?{
        return lng
    }
    @JvmName("getBuka1")
    fun getBuka(): Int?{
        return buka
    }
    @JvmName("getTutup1")
    fun getTutup():Int?{
        return tutup
    }


    //menghitung jarak 2 tempat
    fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double):Double{
        var theta: Double = lng1 - lng2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        dist = dist * 1.609344;
        return dist

    }
    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }
    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    fun distance(xOther: Double, yOther: Double): Double {
        return Math.sqrt((this.lat!! - xOther) * (this.lat!! - xOther) + (this.lng!! - yOther) * (this.lng!! - yOther))*100
    }
    //Total Cost
    fun calculateCost(distance: Double):Double{
        var total:Double = distance * 1500
        return total
    }
}