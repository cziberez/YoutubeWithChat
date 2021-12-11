package hu.unideb.youtube.database.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hu.unideb.youtube.database.model.YoutubeModelEntity

@Database(
    entities = [YoutubeModelEntity::class],
    version = 1,
    exportSchema = false
)
abstract class YoutubeDatabase : RoomDatabase() {

    abstract fun youtubeDao(): YoutubeDao

    companion object {
        @Volatile
        private var INSTANCE: YoutubeDatabase? = null


        fun getDatabase(context: Context): YoutubeDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    YoutubeDatabase::class.java,
                    "youtube_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}