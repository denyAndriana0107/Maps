package unpas.ac.maps.network

import retrofit2.Call
import retrofit2.http.GET
import unpas.ac.id.maps.network.model.KordinatResponse


interface KordinatServices {
    @GET("Wisata")
    fun getData(): Call<KordinatResponse>
}