package hu.unideb.youtube

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.CircleCropTransformation
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import hu.unideb.youtube.model.Models
import hu.unideb.youtube.model.nicknameColor
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryChannelRequest
import io.getstream.chat.android.client.controllers.ChannelController
import io.getstream.chat.android.client.models.Message
import io.getstream.chat.android.client.utils.observable.Subscription
import io.getstream.videochat.R
import kotlin.properties.Delegates

private const val VIDEO_INTENT_EXTRA = "VIDEO_INTENT_EXTRA"
private const val CHANNEL_TYPE = "livestream"

class VideoActivity : AppCompatActivity(R.layout.activity_video) {

    private val messageAdapter: MessageAdapter = MessageAdapter()
    private lateinit var input: EditText
    private lateinit var sendMessageButton: ImageView
    private lateinit var youtubeVideoMessage: RecyclerView
    private lateinit var channelController: ChannelController
    private var subscription: Subscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val video = intent.getSerializableExtra(VIDEO_INTENT_EXTRA) as Models
        val chatClient = ChatClient.instance()
        val findViewById = findViewById<YouTubePlayerView>(R.id.youtubeVideoPlayer)
        findViewById.apply {
            lifecycle.addObserver(this)
            getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(video.id, 0f)
                }
            })
        }
        youtubeVideoMessage =
            findViewById<RecyclerView>(R.id.youtubeVideoMessages).apply { adapter = messageAdapter }
        sendMessageButton = findViewById<ImageView>(R.id.messageSend).apply {
            visibility = View.GONE
            setOnClickListener { sendMessage() }
        }
        input = findViewById<EditText>(R.id.input).apply {
            this.addTextChangedListener {
                sendMessageButton.visibility = if (it.isNullOrBlank()) View.GONE else View.VISIBLE
            }
            chatClient.getCurrentUser()?.getExtraValue("name", "")
                ?.takeIf { it.isNotBlank() }
                ?.let { userName ->
                    val hintValue = "${getString(R.string.chatAs)} $userName..."
                    hint = hintValue
                }
            isEnabled = false
        }
        channelController = chatClient.channel(CHANNEL_TYPE, video.id)
        channelController.query(
            QueryChannelRequest()
                .withData(mapOf("name" to "${getString(R.string.live_for)} '${video.name}'"))
                .withMessages(500)
                .withWatch()
        ).enqueue {
            when (it.isSuccess) {
                true -> {
                    addMessages(it.data().messages)
                    input.isEnabled = true
                }
                false -> {
                    Toast.makeText(applicationContext, it.error().toString(), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        subscription = channelController.events().subscribe { chatEvent ->
            chatEvent
                .takeIf { it.type == "message.new" }
                ?.let { addMessage(it.message) }
        }
    }

    private fun addMessage(message: Message) = addMessages(listOf(message))

    private fun addMessages(messages: List<Message>) {
        messageAdapter.addMessages(messages.map(Message::toViewHolderMessage))
        youtubeVideoMessage.post { youtubeVideoMessage.smoothScrollToPosition(messageAdapter.itemCount) }
    }

    override fun onPause() {
        super.onPause()
        channelController.stopWatching()
        subscription?.unsubscribe()
    }

    private fun sendMessage() {
        input.text
            ?.toString()
            ?.takeIf { it.isNotBlank() }
            ?.let {
                input.setText("")
                channelController.sendMessage(Message().apply { text = it }).enqueue()
            }
    }
}

class MessageAdapter : RecyclerView.Adapter<MessageViewHolder>() {
    private var messages: List<MessageViewHolder.Message> by Delegates.observable(listOf()) { _, _, newMessages ->
        asyncListDiffer.submitList(
            newMessages.takeIf { it.isNotEmpty() })
    }
    private val asyncListDiffer: AsyncListDiffer<MessageViewHolder.Message> by lazy {
        AsyncListDiffer(this, object : DiffUtil.ItemCallback<MessageViewHolder.Message>() {
            override fun areContentsTheSame(
                oldItem: MessageViewHolder.Message,
                newItem: MessageViewHolder.Message
            ): Boolean = oldItem == newItem

            override fun areItemsTheSame(
                oldItem: MessageViewHolder.Message,
                newItem: MessageViewHolder.Message
            ): Boolean = oldItem.id == newItem.id
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder =
        MessageViewHolder(parent)

    override fun getItemCount(): Int = asyncListDiffer.currentList.size
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) =
        holder.bind(asyncListDiffer.currentList[position])

    fun addMessages(newMessages: List<MessageViewHolder.Message>) {
        messages = (messages + newMessages).sortedBy { it.timestamp }
    }
}

class MessageViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
) {
    private val chatAvatar: ImageView by lazy { itemView.findViewById<ImageView>(R.id.chatAvatar) }
    private val chatUserName: TextView by lazy { itemView.findViewById<TextView>(R.id.chatUserName) }
    private val chatMessage: TextView by lazy { itemView.findViewById<TextView>(R.id.chatMessage) }

    fun bind(message: Message) {
        chatUserName.apply {
            text = message.userName
            setTextColor(message.colorName)
        }
        chatMessage.text = message.text
        chatAvatar.load(message.avatarUrl) { transformations(CircleCropTransformation()) }
    }

    data class Message(
        val id: String,
        val timestamp: Long,
        val avatarUrl: String,
        val userName: String,
        @ColorInt val colorName: Int,
        val text: String
    )
}

fun Message.toViewHolderMessage() =
    MessageViewHolder.Message(
        id,
        createdAt?.time ?: 0,
        user.getExtraValue("image", ""),
        user.getExtraValue("name", ""),
        user.nicknameColor,
        text
    )

fun createVideoIntent(context: Context, models: Models) =
    Intent(context, VideoActivity::class.java).apply {
        putExtra(VIDEO_INTENT_EXTRA, models)
    }