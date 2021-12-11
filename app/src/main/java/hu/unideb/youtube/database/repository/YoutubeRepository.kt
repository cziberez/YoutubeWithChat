package hu.unideb.youtube.database.repository

import androidx.lifecycle.LiveData
import hu.unideb.youtube.database.data.YoutubeDao
import hu.unideb.youtube.database.model.YoutubeModelEntity

class YoutubeRepository(private val youtubeDao: YoutubeDao) {

    val readAllData: LiveData<List<YoutubeModelEntity>> = youtubeDao.readAllData()

    fun insertYoutubeData(youtubeModelEntity: YoutubeModelEntity) {
        youtubeDao.insertYoutubeData(youtubeModelEntity)
    }

    fun updateYoutubeData(youtubeModelEntity: YoutubeModelEntity) {
        youtubeDao.updateYoutubeData(youtubeModelEntity)
    }

    fun deleteYoutubeData(youtubeModelEntity: YoutubeModelEntity) {
        youtubeDao.deleteYoutubeData(youtubeModelEntity)
    }

    fun deleteAllYoutubeData() {
        youtubeDao.deleteAllYoutubeData()
    }

}