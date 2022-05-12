package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

private const val TAG = "MainViewModel"

class MainViewModel(application: Application) : ViewModel() {

    private val database = AsteroidDatabase.getDatabase(application)

    private val repository = AsteroidRepository(database.asteroidDao)

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

   val info: LiveData<List<Asteroid>> = Transformations.map(repository.feeds) {
       it
   }


    private val _showProgress = MutableLiveData(true)
    val showProgress: LiveData<Boolean>
        get() = _showProgress

    private val _navigation: MutableLiveData<Asteroid> = MutableLiveData()
    val navigation: LiveData<Asteroid>
        get() = _navigation


    init {
        fetchThePictureOfTheDay()
        loadFeeds()
    }

    private fun loadFeeds() {
        _showProgress.value =  true
        viewModelScope.launch {
            try {
                repository.fetchFeeds()
            }catch (e: Exception) {
                Log.e(TAG, e.message, e.cause)
            }
        }
    }

    private fun fetchThePictureOfTheDay() {
        viewModelScope.launch {
            try {
                val picture = repository.getPictureOfTheDay()
                _pictureOfDay.postValue(picture)
            }catch (e: Exception) {
                Log.e(TAG, e.message, e.cause)
            }
        }
    }


    fun navigateToDetails(asteroid: Asteroid) {
        _navigation.value = asteroid
    }

    fun navigationDone() {
        _navigation.value = null
    }

    fun progress(empty: Boolean) {
        _showProgress.value = empty
    }

    fun filter(filter: Filter) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.filterFeeds(filter)
        }
    }

}