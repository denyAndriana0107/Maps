package danu.ga.maps.fragment.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import danu.ga.maps.R
import danu.ga.maps.api.model.BodyItem
import danu.ga.maps.api.model.KordinatResponse
import danu.ga.maps.databinding.FragmentHomeBinding
import danu.ga.maps.fragment.HomeNav
import danu.ga.maps.fragment.ui.jadwalwisata.SlideshowViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import unpas.ac.id.maps.viewModel.WisataViewModel
import unpas.ac.maps.network.NetworkModule


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    lateinit var wisataViewModel: WisataViewModel
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        wisataViewModel = ViewModelProvider(this).get(WisataViewModel::class.java)
        wisataViewModel.updateData(false)
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        val view = binding.root
        getData()
        return view
    }
    //get data API
    private fun getData(){
        NetworkModule.servicesKordinat().getData().enqueue(object : Callback<KordinatResponse> {
            override fun onResponse(call: Call<KordinatResponse>, response: Response<KordinatResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()?.body
                    if (data?.size ?: 0 > 0) {
                        showData(data)
                    }
                }
            }

            override fun onFailure(call: Call<KordinatResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Can't Connect To Server", Toast.LENGTH_LONG).show()
                Log.d("error server", t.message!!)
            }

        })


    }
    //show data
    private fun showData(data: List<BodyItem?>?) {
        binding.listWisata.adapter = HomeAdapter(data, this.requireContext())
    }
    fun dataCost():Int?{
        var cost = 200
        return cost
    }
}