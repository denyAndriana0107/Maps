package unpas.ac.maps.network

import danu.ga.maps.api.model.KordinatResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface KordinatServices {
    @GET("Wisata")
    fun getData(): Call<KordinatResponse>
    @GET("Majalengka")
    fun getDataMajalengka(): Call<KordinatResponse>
    @GET("Wisata")
    fun getDataPerJam(@Query("Waktu Buka") int: String):Call<KordinatResponse>
}