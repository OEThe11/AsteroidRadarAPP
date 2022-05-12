
package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val API_KEY = "ZllxxsVEP7JgEhsspXng3BQVofbYTrGyU9aB8JgL"


private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)

interface Networking {
       @GET(Endpoints.FEED)
       suspend fun getFeeds (
               @Query("start_date") startDate: String,
               @Query("end_date") endDate: String,
               @Query("api_key") apiKey: String = API_KEY
       ) : String

       @GET(Endpoints.PICTURE_OF_THE_DAY)
       suspend fun getPicture(@Query("api_key") apiKey: String = API_KEY) : PictureOfDay

}

object AsteroidNetworkObj {
        val scalarsNetworkServices: Networking = retrofit.addConverterFactory(
                ScalarsConverterFactory.create()
        ).build().create(Networking::class.java)

        val moshiNetworkServices: Networking = retrofit.addConverterFactory(
                MoshiConverterFactory.create(moshi)
        ).build().create(Networking::class.java)
}