package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid


@Dao
interface AsteroidDao {
    @Query("SELECT * FROM asteroid_feed ORDER BY closeApproachDate DESC")
    fun getAllFeeds(): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroid: Asteroid)

    @Query("SELECT * FROM asteroid_feed WHERE closeApproachDate == :date ORDER BY closeApproachDate DESC")
    suspend fun getFeedByDate(date: String): List<Asteroid>

    @Query("SELECT * FROM asteroid_feed WHERE closeApproachDate BETWEEN :startDate AND :endDate ORDER BY closeApproachDate DESC")
    suspend fun getFeedByWeek(startDate: String, endDate: String): List<Asteroid>
}