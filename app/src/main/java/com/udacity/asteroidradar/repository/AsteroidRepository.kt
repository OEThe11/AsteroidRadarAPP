package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidNetworkObj
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.main.Filter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject


class AsteroidRepository(private val asteroidDao: AsteroidDao) {
    private val allFeeds: LiveData<List<Asteroid>> = asteroidDao.getAllFeeds()
    private val _feeds: MediatorLiveData<List<Asteroid>> = MediatorLiveData()
        val feeds: LiveData<List<Asteroid>>
            get() = _feeds

    init {
        _feeds.addSource(allFeeds) {
            _feeds.value = it
        }
    }

    suspend fun fetchFeeds() {
        val days = getNextSevenDaysFormattedDates()
        val response = AsteroidNetworkObj.scalarsNetworkServices.getFeeds(days.first(), days.last())
        val feeds = parseAsteroidsJsonResult(JSONObject(response))
        asteroidDao.insertAll(*feeds.toTypedArray())

    }

    suspend fun filterFeeds(filter: Filter) {
        val days = getNextSevenDaysFormattedDates()
        val times = when(filter) {
            Filter.TODAY -> asteroidDao.getFeedByDate(days.first())
            Filter.WEEK -> asteroidDao.getFeedByWeek(days.first(), days.last())
            Filter.SAVED -> allFeeds.value
        }
        _feeds.postValue(times)
    }

    suspend fun getPictureOfTheDay(): PictureOfDay {
        return withContext(Dispatchers.IO) {
            val response = AsteroidNetworkObj.moshiNetworkServices.getPicture()
            response
        }
    }


}