package com.muen.sunnyweather.logic.dao

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson
import com.muen.sunnyweather.SunnyWeatherApplication
import com.muen.sunnyweather.logic.model.Place

object PlaceDao {
    private fun sharedPreferences()=SunnyWeatherApplication.context.getSharedPreferences("sunny_weather",
        Context.MODE_PRIVATE)

    fun savePlace(place: Place){
        sharedPreferences().edit().putString("place",Gson().toJson(place)).apply()

    }
    fun getSavedPlace():Place{
        val placeJson= sharedPreferences().getString("place","")
        return Gson().fromJson(placeJson,Place::class.java)
    }
    //判断是否有数据已被存储
    fun isPlaceSaved()= sharedPreferences().contains("place")
}