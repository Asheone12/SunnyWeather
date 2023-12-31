package com.muen.sunnyweather.logic.network

import com.muen.sunnyweather.SunnyWeatherApplication
import com.muen.sunnyweather.logic.model.DailyResponse
import com.muen.sunnyweather.logic.model.RealTimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    @GET("v2.6/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(@Path("lng")lng:String,@Path("lat")lat:String):Call<RealTimeResponse>

    @GET("v2.6/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng")lng:String,@Path("lat")lat:String):Call<DailyResponse>
}