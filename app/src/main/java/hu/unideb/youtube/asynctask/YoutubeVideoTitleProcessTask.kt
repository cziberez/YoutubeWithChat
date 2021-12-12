package hu.unideb.youtube.asynctask

import android.os.AsyncTask
import hu.unideb.youtube.retrofit.RetrofitClient
import hu.unideb.youtube.retrofit.YoutubeApiCalls

/**
 * Youtube videó címeket feldolgozó Async Task.
 */
internal class YoutubeVideoTitleProcessTask : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg videoIds: String): String {
        var titleValue = ""
        val videoId = videoIds[0]
        val request = RetrofitClient.buildService(YoutubeApiCalls::class.java)
        val call =
            request.getVideoInformation("https://www.youtube.com/oembed?url=youtube.com/watch?v=$videoId&format=json")
        val response = call.execute()
        if (response.isSuccessful) {
            titleValue = response.body()?.title ?: ""
        }
        return titleValue
    }
}