package hu.unideb.youtube.database.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.unideb.youtube.database.model.YoutubeModelEntity
import io.getstream.videochat.R

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var youtubeVideoList = emptyList<YoutubeModelEntity>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.custom_row,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return youtubeVideoList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = youtubeVideoList[position]

        holder.itemView.findViewById<TextView>(R.id.id_txt).text = (position + 1).toString()
        holder.itemView.findViewById<TextView>(R.id.videoId_txt).text = currentItem.videoId
    }

    fun setData(user: List<YoutubeModelEntity>) {
        this.youtubeVideoList = user
        notifyDataSetChanged()
    }
}