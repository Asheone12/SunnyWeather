package com.muen.sunnyweather.logic.model

//封装DailyResponse和RealTimeResponse
data class Weather(val realTime: RealTimeResponse.RealTime,val daily:DailyResponse.Daily)