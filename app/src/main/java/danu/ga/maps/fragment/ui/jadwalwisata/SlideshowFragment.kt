package danu.ga.maps.fragment.ui.jadwalwisata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import danu.ga.maps.R
import danu.ga.maps.api.model.KordinatResponse
import danu.ga.maps.databinding.FragmentSlideshowBinding
import danu.ga.maps.fragment.HomeNav
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import unpas.ac.id.maps.viewModel.WisataViewModel
import unpas.ac.maps.network.NetworkModule
import java.lang.Exception
import java.util.ArrayList


class SlideshowFragment : Fragment() {
    private lateinit var wisataViewModel: WisataViewModel
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var binding: FragmentSlideshowBinding
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        wisataViewModel =
                ViewModelProvider(this).get(WisataViewModel::class.java)

        binding = FragmentSlideshowBinding.inflate(inflater,container,false)
        val view = binding.root
        wisataViewModel.updateData(false)
        mapView = view.findViewById(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.onResume()

        try {
            MapsInitializer.initialize(requireContext())
        }catch (e:Exception){
            e.printStackTrace()
        }
        val spinner:Spinner = binding.spinnerLisJadwalWisata
        val list = arrayOf("Pilih Jam Buka",7,8,9,10)
        val arrayAdapter= ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,list)
        spinner.adapter = arrayAdapter
        mapView.getMapAsync(OnMapReadyCallback{
            googleMap = it
            spinner.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    googleMap.clear()
                    getData(list[p2].toString())
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    googleMap.clear()
                }

            }


        })

        return view
    }

    fun getData(int: String){
        NetworkModule.servicesKordinat().getDataPerJam(int).enqueue(object : Callback<KordinatResponse> {
            override fun onResponse(call: Call<KordinatResponse>, response: Response<KordinatResponse>) {
                if(response.isSuccessful){
                    val data = response.body()?.body
                    if (data?.size ?: 0 > 0) {
                        val index: Int = data!!.size
                        var angka = 0
                        var myMarker1 = LatLng(-0.0, 0.0)
                        var marker:Marker ?= null
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
                            angka++
                        }
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlngs[0], 20.0f))
                        googleMap.animateCamera(CameraUpdateFactory.zoomIn())
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f), 2000, null)
                    }
                }
            }

            override fun onFailure(call: Call<KordinatResponse>, t: Throwable) {
                Toast.makeText(requireContext(),"Connecting data failed",Toast.LENGTH_LONG).show()
            }

        })
    }
}