package danu.ga.maps.fragment.ui.maps

import android.annotation.SuppressLint
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import danu.ga.maps.R
import danu.ga.maps.api.model.KordinatResponse
import danu.ga.maps.fragment.HomeNav
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import unpas.ac.id.maps.data.Wisata
import unpas.ac.id.maps.viewModel.WisataViewModel
import unpas.ac.maps.algoritm.City
import unpas.ac.maps.algoritm.Population
import unpas.ac.maps.network.NetworkModule
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class MapsFragment2 : Fragment() {
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var settingClient: SettingsClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationSettingRequest: LocationSettingsRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var currentLocation: Location
    private var marker: Marker?= null
    private lateinit var kordinatUpdate:LatLng
    private var listMutasiKordinatPath:MutableList<LatLng> = mutableListOf()
    private var polyline:Polyline ?= null
    private var cost:Int ?= null
    lateinit var wisataViewModel: WisataViewModel
    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        wisataViewModel = ViewModelProvider(this).get(WisataViewModel::class.java)
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location->
            if (location==null){
                Toast.makeText(
                        this.requireContext(),
                        "Check You're Connection or Enable Your Location ",
                        Toast.LENGTH_LONG
                ).show()
            }
            else {
                val setFragment = HomeNav()
                val myLocation = LatLng(location.latitude, location.longitude)
                //Mutasi

                var jumlah:Int = 0
                var pathArray:Array<City> = Array(14){ City(0, 0.0, 0.0,0,0) }
                pathArray[0]= City(1 , -7.141948728625862, 107.42001699715476, 7,17)
                pathArray[1]= City(2, -7.13786837250741, 107.4003322683194, 7, 17)
                pathArray[2]= City(3, -7.147259667366287, 107.43302599715466, 7, 17)
                pathArray[3]= City(4, -7.140309854149551, 107.40050826831946, 7, 17)
                pathArray[4]= City(5, -7.134110954021378, 107.42146499715453, 8, 21)
                pathArray[5]= City(6, -7.1263228460283345, 107.4213933529785, 8, 21)
                pathArray[6]= City(7, -7.131829661631447, 107.42214335297857, 8, 21)
                pathArray[7]= City(8, -7.134542142316982, 107.42175399715457, 8, 21)
                pathArray[8]= City(9, -7.162362963217781, 107.43179799715483, 9, 17)
                pathArray[9]= City(10, -7.158514509943944, 107.41803662414344, 9, 21)
                pathArray[10]= City (11, -7.133991370886841, 107.42228635297856, 9, 17)
                pathArray[11]= City(12, -7.127602305985058, 107.43041099715458, 10, 21)
                pathArray[12]= City(13, -7.127168138379776, 107.43032926831937, 10, 21)
                pathArray[13]= City( 14, -7.130162723424981, 107.43236826831934, 10, 21)

                val currentTimestamp = System.currentTimeMillis()
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("HH.mm")
                val formatted = current.format(formatter)
                val time:Double = formatted.toDouble()
                for (i in 0 until pathArray.size){
                    if (pathArray[i].tutup!! > time && pathArray[i].buka!! < time){
                        jumlah ++
                    }
                }

                //seleksi jumlah
                listMutasiKordinatPath = ArrayList(jumlah+1)

                if(jumlah != 0 ){
                    var numberOfGenerations:Int = 0
                    val stopAt:Int= 2500
                    val GAUSE = true
                    val pop: Population = Population(30,jumlah, 1.0, 1.0)
                    if (GAUSE){
                        pop.FitnessOrder()
                    }
                    while (numberOfGenerations != stopAt) {
                        //Select / Crossover
                        while (pop.Mate() == false);
                        //Mutate
                        for (i in 0 until pop.getNextGen().size) {
                            pop.getNextGen()[i]!!.setPath(pop.Mutation(pop.getNextGen()[i]!!.getPath()))
                        }

                        //Setting the new Generation to current Generation
                        pop.setPopulation(pop.getNextGen())
                        pop.setDone(0)
                        //Sorting the new population from Finess / Evaluating
                        pop.FitnessOrder()
                        //Incremente number of Generations
                        numberOfGenerations++
                    }
                    for (j in 0 until pop.getPopulation()[pop.getPopulationSize()-1]!!.getPath().size){
                        listMutasiKordinatPath.add(LatLng(pop.getPopulation()[pop.getPopulationSize()-1]!!.getPath()[j]!!.getLat()!!.toDouble(),
                                pop.getPopulation()[pop.getPopulationSize()-1]!!.getPath()[j]!!.getLng()!!.toDouble()))
                    }

                    //add data to database
                    cost = pop.getPopulation()[pop.getPopulationSize() - 1]?.getCost()!!.roundToInt()*1000
                    val number:Double = pop.getPopulation()[pop.getPopulationSize()-1]!!.getCost()!!
                    val number3digits:Double = Math.round(number * 1000.0)/1000.0
                    val number2digits:Double = Math.round(number3digits * 100.0)/100.0
                    wisataViewModel.readData.observe(this, Observer {
                        if (it.size==0){
                            wisataViewModel.insertData(Wisata(1,true,cost!!,number2digits))
                        }else{
                            wisataViewModel.updateCost(cost!!,number2digits)
                        }
                    })
                    wisataViewModel.updateData(true)
                    NetworkModule.servicesKordinat().getData().enqueue(object :
                            Callback<KordinatResponse> {
                        override fun onResponse(
                                call: Call<KordinatResponse>,
                                response: Response<KordinatResponse>
                        ) {
                            if (response.isSuccessful) {
                                val data = response.body()?.body
                                if (data?.size ?: 0 > 0) {
                                    val index: Int = data!!.size
                                    var angka = 0
                                    var myMarker1 = LatLng(-0.0, 0.0)


                                    //add data to map
                                    var latlngs: MutableList<LatLng> = mutableListOf()
                                    while (angka < index) {
                                        latlngs.add(
                                                LatLng(data[angka]?.lat!!.toDouble(),
                                                        data[angka]?.lng!!.toDouble())
                                        )
                                        myMarker1 = LatLng(
                                                data[angka]?.lat!!.toDouble(),
                                                data[angka]?.lng!!.toDouble()
                                        )
                                        mMap.addMarker(
                                                MarkerOptions().position(myMarker1)
                                                        .title(data[angka]?.nama!!)
                                        )
                                        angka++
                                    }
                                    mMap.addPolyline(PolylineOptions()
                                            .add(myLocation)
                                            .addAll(listMutasiKordinatPath))
                                } else {
                                    print("Cau")

                                }
                            }
                        }

                        override fun onFailure(call: Call<KordinatResponse>, t: Throwable) {
                            print("Error")
                        }

                    })
                    //add Marker )
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 10.0f))
                    mMap.animateCamera(CameraUpdateFactory.zoomIn())
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f), 2000, null)
                    val cameraPosition = CameraPosition.Builder()
                            .target(myLocation) // Sets the center of the map to Mountain View
                            .zoom(17f) // Sets the zoom
                            .bearing(90f) // Sets the orientation of the camera to east
                            .tilt(30f) // Sets the tilt of the camera to 30 degrees
                            .build() // Creates a CameraPosition from the builder

                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    locationRequest()
                    locationCallback()
                    buildSettingLocationRequet()
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,
                            Looper.getMainLooper())
                    }else{
                        NetworkModule.servicesKordinat().getData().enqueue(object :Callback<KordinatResponse>{
                            override fun onResponse(call: Call<KordinatResponse>, response: Response<KordinatResponse>) {
                                if(response.isSuccessful){
                                    val data = response.body()?.body
                                    if (data?.size ?: 0 > 0) {
                                        val index: Int = data!!.size
                                        var angka = 0
                                        var myMarker1 = LatLng(-0.0, 0.0)
                                        //add data to map
                                        var latlngs: MutableList<LatLng> = mutableListOf()
                                        while (angka < index) {
                                            latlngs.add(
                                                    LatLng(data[angka]?.lat!!.toDouble(),
                                                            data[angka]?.lng!!.toDouble())
                                            )
                                            myMarker1 = LatLng(
                                                    data[angka]?.lat!!.toDouble(),
                                                    data[angka]?.lng!!.toDouble()
                                            )
                                            mMap.addMarker(
                                                    MarkerOptions().position(myMarker1)
                                                            .title(data[angka]?.nama!!)
                                            )
                                            angka++
                                        }
                                    }
                                }
                            }

                            override fun onFailure(call: Call<KordinatResponse>, t: Throwable) {
                                TODO("Not yet implemented")
                            }

                        })

                    //add Marker )
                    mMap.addMarker(MarkerOptions().position(myLocation).title("My Location"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 10.0f))
                    mMap.animateCamera(CameraUpdateFactory.zoomIn())
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f), 2000, null)


                    // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
                    val cameraPosition = CameraPosition.Builder()
                            .target(myLocation) // Sets the center of the map to Mountain View
                            .zoom(17f) // Sets the zoom
                            .bearing(90f) // Sets the orientation of the camera to east
                            .tilt(30f) // Sets the tilt of the camera to 30 degrees
                            .build() // Creates a CameraPosition from the builder

                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    }
                }





        }


//

    }
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

       // FusedClientGPS
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireContext())
        settingClient = LocationServices.getSettingsClient(this.requireContext())
    }


    //function
    private fun locationRequest(){
        locationRequest = LocationRequest()
        locationRequest.setInterval(4000)
        locationRequest.setFastestInterval(2000)
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

    }
    private fun locationCallback(){
        locationCallback = object :LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    currentLocation = locationResult.lastLocation
                    updateLocationUI()
                }
            }
        }
    }
    private fun buildSettingLocationRequet(){
        var builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        locationSettingRequest = builder.build()
    }
    private fun updateLocationUI(){
        if (marker==null && polyline==null){
            kordinatUpdate = LatLng(currentLocation.latitude,currentLocation.longitude)
            var a = MarkerOptions().position(kordinatUpdate)
            marker = mMap.addMarker(a.title("My Location"))
            NetworkModule.servicesKordinat().getData().enqueue(object :
                    Callback<KordinatResponse> {
                override fun onResponse(
                        call: Call<KordinatResponse>,
                        response: Response<KordinatResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()?.body
                        if (data?.size ?: 0 > 0) {
                            val index: Int = data!!.size
                            var angka = 0
                            var myMarker1 = LatLng(-0.0, 0.0)


                            //add data to map
                            var latlngs: MutableList<LatLng> = mutableListOf()
                            while (angka < index) {
                                latlngs.add(
                                        LatLng(data[angka]?.lat!!.toDouble(),
                                                data[angka]?.lng!!.toDouble())
                                )
                                myMarker1 = LatLng(
                                        data[angka]?.lat!!.toDouble(),
                                        data[angka]?.lng!!.toDouble()
                                )
                                mMap.addMarker(
                                        MarkerOptions().position(myMarker1)
                                                .title(data[angka]?.nama!!)
                                )
                                angka++
                            }
                            listMutasiKordinatPath.add(0,kordinatUpdate)
                            polyline = mMap.addPolyline(PolylineOptions().addAll(listMutasiKordinatPath))
                        } else {
                            print("Cau")

                        }
                    }
                }

                override fun onFailure(call: Call<KordinatResponse>, t: Throwable) {
                    print("Error")
                }

            })
        }else{
            kordinatUpdate = LatLng(currentLocation.latitude,currentLocation.longitude)
            marker!!.setPosition(kordinatUpdate)
            listMutasiKordinatPath.set(0,kordinatUpdate)
            polyline?.setPoints(listMutasiKordinatPath)

        }

    }

    fun dataCost():Int?{
        return this.cost?.plus(100)
    }

}