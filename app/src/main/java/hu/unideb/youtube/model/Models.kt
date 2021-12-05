package hu.unideb.youtube.model

import android.graphics.Color
import hu.unideb.youtube.asynctask.YoutubeVideoTitleTask
import io.getstream.chat.android.client.models.User
import java.io.Serializable

/**
 * A videókat reprezentáló model.
 */
data class Video(val id: String, val name: String, val image: String) : Serializable

/**
 * A betöltendő videó azonosítói.
 */
val videoIds = listOf(
    "w-8WOn-i4jg",
    "KSDZ6gchVXs",
    "7YDWUF5wM5Y",
    "u7ERny75dik",
    "QcxZ6CXE6_U",
    "T-i6AHY3Kgk",
    "ef2smv1Hrcc",
    "OKaWOuykWO4"
)

val videos = getVideosById()

private fun getVideosById(): List<Video> {
    val videoList = mutableListOf<Video>()
    for (videoId in videoIds) {
        val video = createVideo(videoId);
        videoList.add(video);
    }
    return videoList;
}

/**
 * Létrehozza a video data-t.
 */
private fun createVideo(videoId: String): Video {
    return Video(
        videoId,
        getNameFromVideoId(videoId),
        "https://img.youtube.com/vi/$videoId/default.jpg"
    )
}

/**
 * Teszt tokenek a chathez.
 */
private val tokensMap = mapOf(
    "tesztelek" to "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoiaGVhdnljYXQzMTgifQ.ZZoXIey7j0Lz6v8SJc84vTPY8s05Jauot3T4ysUEEA8",
    "tesztsanyi" to "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoic21hbGxkb2cxMTQifQ.pw0O7BQ3rBTM3Ph8VzM5oez3axZl_3p5kv3u1xnLA5k"
)

/**
 * Demó felhasználók listája.
 */
val demoUsers = listOf(
    User("tesztelek").apply {
        extraData["name"] = "Teszt Elek"
        extraData["image"] = "https://randomuser.me/api/portraits/thumb/men/75.jpg"
    },
    User("tesztsanyi").apply {
        extraData["name"] = "Teszt Sanyi"
        extraData["image"] = "https://randomuser.me/api/portraits/thumb/men/80.jpg"
    }
)

/**
 * A chat színei.
 */
val randomColors = listOf(
    Color.parseColor("#e483c9"),
    Color.parseColor("#7a822b"),
    Color.parseColor("#1f926d"),
    Color.parseColor("#cfd78d"),
    Color.parseColor("#d5a879"),
    Color.parseColor("#43356e"),
    Color.parseColor("#06b185"),
    Color.parseColor("#9a0b9e"),
    Color.parseColor("#60bd55"),
    Color.parseColor("#2f580d"),
    Color.parseColor("#3c383f"),
    Color.parseColor("#9edb30"),
    Color.parseColor("#ce7b23"),
    Color.parseColor("#faecea"),
    Color.parseColor("#ba2929")
)

val userColorMap = mutableMapOf<String, Int>()
val User.nicknameColor: Int
    get() = userColorMap.getOrPut(id) { randomColors.random() }

val User.token: String
    get() = tokensMap[id] ?: ""

private fun getNameFromVideoId(videoId: String): String {
    return YoutubeVideoTitleTask().execute("https://www.youtube.com/oembed?url=youtube.com/watch?v=$videoId&format=json")
        .get()
}

