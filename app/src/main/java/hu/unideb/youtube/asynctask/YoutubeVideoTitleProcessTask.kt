package hu.unideb.youtube.asynctask

import android.os.AsyncTask
import org.json.JSONException
import org.json.JSONObject
import java.net.URL

/**
 * Youtube videó címeket feldolgozó Async Task.
 */
internal class YoutubeVideoTitleProcessTask : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg videoIds: String): String? {
        return try {
            val videoId = videoIds[0]
            val apiResponse =
                URL("https://www.youtube.com/oembed?url=youtube.com/watch?v=$videoId&format=json").readText()
            val parsedJson = parseJson(apiResponse)
            return parsedJson?.getString("title").toString()
        } catch (e: Exception) {
            e.printStackTrace().toString()
        }
    }

    private fun parseJson(json: String): JSONObject? {

        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(json)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonObject
    }
}