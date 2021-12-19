package unpas.ac.maps.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import unpas.ac.id.maps.network.Constant.Companion.BASE_URL


class NetworkModule {
    companion object{

        //cek Request data JSON
        fun interceptor(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            return OkHttpClient.Builder().addInterceptor(interceptor).build()
        }

        //Get Data Json
        fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(interceptor())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        fun servicesKordinat() = getRetrofit().create(KordinatServices::class.java)
    }
}