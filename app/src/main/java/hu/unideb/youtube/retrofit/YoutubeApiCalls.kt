package hu.unideb.youtube.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface YoutubeApiCalls {

    @GET
    fun getVideoInformation(
        @Url fullUrl: String,
    ): Call<YoutubeVideoDataResponse>

}