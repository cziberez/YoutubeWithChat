package hu.unideb.youtube

import android.content.Context
import android.content.Intent
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hu.unideb.youtube.model.Models
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

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { startActivity(createRecommendationIntent(this)) }

    }

    private fun onVideoSelected(models: Models) {
        startActivity(createVideoIntent(this, models))
    }
}

private fun createRecommendationIntent(context: Context) =
    Intent(context, RecomendationActivity::class.java).apply {}

class VideoAdapter(private val onVideoSelected: (Models) -> Unit) :
    RecyclerView.Adapter<VideoViewHolder>() {
    private var models: List<Models> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder =
        VideoViewHolder(parent, onVideoSelected)

    override fun getItemCount(): Int = models.size
    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) =
        holder.bind(models[position])

    fun addVideos(newModels: List<Models>) {
        models = models + newModels
        notifyItemRangeInserted(models.size - newModels.size, newModels.size)
    }
}

class VerticalDividerItemDecorator(context: Context) : DividerItemDecoration(context, VERTICAL) {
    init {
        ContextCompat.getDrawable(context, R.drawable.vertical_divider)?.let(::setDrawable)
    }
}


class VideoViewHolder(
    parent: ViewGroup,
    private val onVideoSelected: (Models) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
) {
    private val youtubeVideo: ImageView by lazy { itemView.findViewById(R.id.ytVideo) }
    private val youtubeVideoName: TextView by lazy { itemView.findViewById(R.id.ytVideoName) }

    fun bind(models: Models) {
        youtubeVideoName.text = models.name
        youtubeVideo.load(models.image)
        itemView.setOnClickListener { onVideoSelected(models) }
    }
}