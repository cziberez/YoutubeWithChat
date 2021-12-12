package hu.unideb.youtube.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val INSTANCE: Retrofit = getRetrofitInstance()

    private fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(
            "https://www.youtube.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> buildService(service: Class<T>): T {
        return INSTANCE.create(service)
    }

}