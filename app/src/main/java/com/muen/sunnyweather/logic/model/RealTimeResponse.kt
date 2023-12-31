package com.muen.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName

data class RealTimeResponse(val status:String,val result:Result) {
    data class Result(val realtime:RealTime)

    data class RealTime(val skycon:String,val temperature:Float,
        @SerializedName("air_quality") val airQuality:AirQuality)

    data class AirQuality(val aqi:AQI)

    data class AQI(val chn:Float)
}