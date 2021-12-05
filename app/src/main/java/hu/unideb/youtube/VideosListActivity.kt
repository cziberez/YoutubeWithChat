package hu.unideb.youtube

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import hu.unideb.youtube.model.Video
import hu.unideb.youtube.model.demoUsers
import hu.unideb.youtube.model.token
import hu.unideb.youtube.model.videos
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.errors.ChatError
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.socket.InitConnectionListener
import io.getstream.videochat.R

/**
 * Demo api kulcs. Lejár: 2021.01.05-én
 */
private const val APIKEY = "sfgpnf7xhf2r"

class VideosListActivity : AppCompatActivity(R.layout.activity_videos_list) {

    private val videosAdapter = VideoAdapter(::onVideoSelected)

    private lateinit var client: ChatClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<RecyclerView>(R.id.youtubeVideos).apply {
            adapter = videosAdapter
            addItemDecoration(VerticalDividerItemDecorator(context))
        }
        val progressBar: ProgressBar = findViewById(R.id.progressBar)

        val user = demoUsers.random()
        client = ChatClient.Builder(APIKEY, this.applicationContext)
            .logLevel(ChatLogLevel.ALL)
            .build()

        client.setUser(user, user.token, object : InitConnectionListener() {
            override fun onSuccess(data: ConnectionData) {
                videosAdapter.addVideos(videos)
                progressBar.visibility = View.GONE
            }

            override fun onError(error: ChatError) {
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun onVideoSelected(video: Video) {
        startActivity(createVideoIntent(this, video))
    }
}

class VideoAdapter(private val onVideoSelected: (Video) -> Unit) :
    RecyclerView.Adapter<VideoViewHolder>() {
    private var videos: List<Video> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder =
        VideoViewHolder(parent, onVideoSelected)

    override fun getItemCount(): Int = videos.size
    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) =
        holder.bind(videos[position])

    fun addVideos(newVideos: List<Video>) {
        videos = videos + newVideos
        notifyItemRangeInserted(videos.size - newVideos.size, newVideos.size)
    }
}

class VerticalDividerItemDecorator(context: Context) : DividerItemDecoration(context, VERTICAL) {
    init {
        ContextCompat.getDrawable(context, R.drawable.vertical_divider)?.let(::setDrawable)
    }
}


class VideoViewHolder(
    parent: ViewGroup,
    private val onVideoSelected: (Video) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.fragment_video, parent, false)
) {
    private val youtubeVideo: ImageView by lazy { itemView.findViewById<ImageView>(R.id.ytVideo) }
    private val youtubeVideoName: TextView by lazy { itemView.findViewById<TextView>(R.id.ytVideoName) }

    fun bind(video: Video) {
        youtubeVideoName.text = video.name
        youtubeVideo.load(video.image)
        itemView.setOnClickListener { onVideoSelected(video) }
    }
}