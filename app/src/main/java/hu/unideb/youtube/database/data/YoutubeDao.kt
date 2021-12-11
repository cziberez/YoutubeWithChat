package hu.unideb.youtube.database.data

import androidx.lifecycle.LiveData
import androidx.room.*
import hu.unideb.youtube.database.model.YoutubeModelEntity

@Dao
interface YoutubeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertYoutubeData(yotubeEntity: YoutubeModelEntity)

    @Update
    fun updateYoutubeData(yotubeEntity: YoutubeModelEntity)

    @Delete
    fun deleteYoutubeData(yotubeEntity: YoutubeModelEntity)

    @Query("DELETE FROM YoutubeModelEntity")
    fun deleteAllYoutubeData()

    @Query("SELECT * FROM YoutubeModelEntity")
    fun readAllData(): LiveData<List<YoutubeModelEntity>>

}