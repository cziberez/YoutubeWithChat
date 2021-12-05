package hu.unideb.youtube.asynctask

import android.os.AsyncTask
import org.json.JSONException
import org.json.JSONObject
import java.net.URL

/**
 * Youtube videó címeket feldolgozó Async Task.
 */
internal class YoutubeVideoTitleTask : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg urls: String): String? {
        return try {
            val apiResponse = URL(urls[0]).readText()
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