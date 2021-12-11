package hu.unideb.youtube.database.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class YoutubeModelEntity(
    @PrimaryKey(autoGenerate = false)
    val videoId: String) : Parcelable
