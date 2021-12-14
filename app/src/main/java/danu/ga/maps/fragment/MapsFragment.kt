package danu.ga.maps.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import danu.ga.maps.MainActivity
import danu.ga.maps.R
import danu.ga.maps.databinding.FragmentMapsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import unpas.ac.id.maps.data.Wisata
import unpas.ac.id.maps.network.model.KordinatResponse
import unpas.ac.id.maps.viewModel.WisataViewModel
import unpas.ac.maps.algoritm.City
import unpas.ac.maps.algoritm.Population
import unpas.ac.maps.network.NetworkModule
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MapsFragment : Fragment() {
    lateinit var binding: FragmentMapsBinding
    lateinit var wisataViewModel: WisataViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var array: Array<City?>
    private var sizeArray:Int = 0
    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->


//        val sydney = LatLng(-34.0, 151.0)
//        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        wisataViewModel.readData.observe(viewLifecycleOwner,{wisata->
            sizeArray = wisata.size

        })

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            if(location==null){
                Toast.makeText(
                        this.requireContext(),
                        "Check You're Connection or Enable Your Location ",
                        Toast.LENGTH_LONG
                ).show()
            }else{
                val myLocation = LatLng(location!!.latitude, location!!.longitude)
                //Mutasi

                var jumlah:Int = 0
                var pathArray:Array<City> = Array(14){ City(0, 0.0, 0.0,0,0) }
//                pathArray[0] = City(1,-6.837117742309791,108.23474243750054,8,15)
//                pathArray[1] = City(2,-6.833511818974356,108.23374125613613,7,22)
//                pathArray[2] = City(3,-6.832790326613714,108.21439662980889,7,18)
//                pathArray[3] = City(4,-6.835272384200842,108.22775403108928,7,20)
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
                val time:Double = 7.1
                for (i in 0 until pathArray.size){
                    if (pathArray[i].tutup!! > time && pathArray[i].buka!! < time){
                        jumlah ++
                    }
                }
                print("jumlah adalah $jumlah")
                Toast.makeText(requireContext(),"$jumlah",Toast.LENGTH_LONG).show()


                var numberOfGenerations:Int = 4
                val stopAt:Int= 2500
                val GAUSE = true
                val pop: Population = Population(4,jumlah, 1.0, 0.5)
                if (GAUSE){
                    pop.FitnessOrder()
                }
                while (numberOfGenerations !== stopAt) {
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
//                for (i in 0 until pop.getPopulation().size) {
//                    println("Path ID: " + i + " | Cost: " + pop.getPopulation()[i]!!.getCost() + " | Fitness: " + pop.getPopulation()[i]!!.getFitness() + "%")
//                    print("Path is: ")
//                    for (j in 0 until pop.getPopulation()[i]!!.getPath().size) {
//                        print(pop.getPopulation()[i]!!.getPath()[j]!!.id.toString() + "(" + pop.getPopulation()[i]!!.getPath()[j]!!.getLat() + "," + pop.getPopulation()[i]!!.getPath()[j]!!.getLng() + ")  ")
//                        Toast.makeText(this.requireContext(),pop.getPopulation()[i]!!.getPath()[j]!!.id.toString() + "(" + pop.getPopulation()[i]!!.getPath()[j]!!.getLat() + "," + pop.getPopulation()[i]!!.getPath()[j]!!.getLng() + ")  ",Toast.LENGTH_LONG).show()
//                    }
//                    println("\n -----------------------------------------------------")
//                }
//
                val listMutasiKordinatPath:MutableList<LatLng> = mutableListOf()
                for (j in 0 until pop.getPopulation()[pop.getPopulationSize()-1]!!.getPath().size){
                    listMutasiKordinatPath.add(LatLng(pop.getPopulation()[pop.getPopulationSize()-1]!!.getPath()[j]!!.getLat()!!.toDouble(),
                            pop.getPopulation()[pop.getPopulationSize()-1]!!.getPath()[j]!!.getLng()!!.toDouble()))

                }
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
                                    googleMap.addMarker(
                                            MarkerOptions().position(myMarker1)
                                                    .title(data[angka]?.nama!!)
                                    )
//                                    latlngs.add(
//                                            LatLng(pop.getPopulation()[3]!!.getPath()[1]!!.getLat()!!.toDouble()
//                                                    ,pop.getPopulation()[3]!!.getPath()[1]!!.getLng()!!.toDouble())
//                                    )
                                    angka++
                                }
                                googleMap.addPolyline(PolylineOptions()
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
                //add Marker
                googleMap.addMarker(MarkerOptions().position(myLocation).title("My Location"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 10.0f))
                googleMap.animateCamera(CameraUpdateFactory.zoomIn())
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f), 2000, null)


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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_maps, container, false)
        binding = FragmentMapsBinding.bind(view)

        //viewModel database
        wisataViewModel = ViewModelProvider(this).get(WisataViewModel::class.java)

        //API to database
        insertAPItoDatabase()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        //GetLoc
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireContext())

    }


    //functions

    //insert API to database
    private fun insertAPItoDatabase(){
        NetworkModule.servicesKordinat().getData().enqueue(object : Callback<KordinatResponse> {
            override fun onResponse(
                call: Call<KordinatResponse>,
                response: Response<KordinatResponse>
            ) {
                if (response.isSuccessful){
                    val data = response.body()?.body
                    if (data?.size ?:0 > 0){
                        var i = 0;
                        while (i < data!!.size){
                            var id:Int = data[i]?.id!!.toInt()
                            var nama:String = data[i]?.nama!!.toString()
                            var lat:Double = data[i]?.lat!!.toDouble()
                            var lng:Double = data[i]?.lng!!.toDouble()
                            var buka:Int = data[i]?.waktuBuka!!.toInt()
                            var tutup:Int = data[i]?.waktuTutup!!.toInt()
                            wisataViewModel.insertData(Wisata(id,nama,lat,lng,buka,tutup))
                            i++
                        }
                    }else{
                        Toast.makeText(context,"Empty Data", Toast.LENGTH_LONG).show()
                    }
                }

            }

            override fun onFailure(call: Call<KordinatResponse>, t: Throwable) {
                Toast.makeText(context,"Check Your Connection", Toast.LENGTH_LONG).show()
            }

        })
    }

    fun data():Array<City?>{
        this.array = emptyArray()
        wisataViewModel =  ViewModelProvider(this).get(WisataViewModel::class.java)
        wisataViewModel.readData.observe(viewLifecycleOwner,{wisata->
            for (i in 0 until wisata.size){
                this.array[i] = City(wisata[i].id,wisata[i].lat,wisata[i].lng,
                    wisata[i].buka,wisata[i].tutup)
            }
        })
        print(array.size)
        return this.array
    }


}