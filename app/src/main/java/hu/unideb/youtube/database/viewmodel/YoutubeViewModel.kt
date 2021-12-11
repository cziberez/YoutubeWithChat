package hu.unideb.youtube.database.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import hu.unideb.youtube.database.data.YoutubeDatabase
import hu.unideb.youtube.database.model.YoutubeModelEntity
import hu.unideb.youtube.database.repository.YoutubeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class YoutubeViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<YoutubeModelEntity>>

    private val repository: YoutubeRepository

    init {
        val youtubeDao = YoutubeDatabase.getDatabase(application).youtubeDao()
        repository = YoutubeRepository(youtubeDao)
        readAllData = repository.readAllData
    }

    fun insertYoutubeData(youtubeModelEntity: YoutubeModelEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertYoutubeData(youtubeModelEntity)
        }
    }

    fun updateYoutubeData(youtubeModelEntity: YoutubeModelEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateYoutubeData(youtubeModelEntity)
        }
    }

    fun deleteYoutubeData(youtubeModelEntity: YoutubeModelEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteYoutubeData(youtubeModelEntity)
        }
    }

    fun deleteAllYoutubeData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllYoutubeData()
        }
    }

}