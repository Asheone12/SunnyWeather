package com.muen.sunnyweather.logic

import androidx.lifecycle.liveData
import com.muen.sunnyweather.logic.dao.PlaceDao
import com.muen.sunnyweather.logic.model.Place
import com.muen.sunnyweather.logic.model.Weather
import com.muen.sunnyweather.logic.network.SunnyWeatherNetWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

object Repository {
    fun searchPlaces(query:String)= fire(Dispatchers.IO) {
            val placeResponse=SunnyWeatherNetWork.searchPlaces(query)
            if(placeResponse.status=="ok"){
                val places=placeResponse.places
                Result.success(places)
            }else{
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
    }

    fun refreshWeather(lng:String,lat:String)= fire(Dispatchers.IO) {
            coroutineScope {
                val deferredRealtime=async {
                    SunnyWeatherNetWork.getRealtimeWeather(lng,lat)
                }
                val deferredDaily=async {
                    SunnyWeatherNetWork.getDailyWeather(lng,lat)
                }
                val realTimeResponse=deferredRealtime.await()
                val dailyResponse=deferredDaily.await()
                if(realTimeResponse.status=="ok" && dailyResponse.status == "ok"){
                    val weather=Weather(realTimeResponse.result.realtime,
                        dailyResponse.result.daily)
                Result.success(weather)
                }else{
                    Result.failure(
                        RuntimeException(
                            "realtime response status is ${realTimeResponse.status}"+
                            "daily response status is ${dailyResponse.status}"
                        )
                    )
                }
            }
    }

    fun savePlace(place:Place)=PlaceDao.savePlace(place)

    fun getSavedPlace()=PlaceDao.getSavedPlace()

    fun isPlaceSaved()=PlaceDao.isPlaceSaved()

    //统一进行try-catch处理
    private fun<T> fire(context: CoroutineContext,block:suspend ()->Result<T>)=
        liveData<Result<T>>(context){
            val result=try {
                block()
            }catch (e:Exception){
                Result.failure<T>(e)
            }
            emit(result)
        }
}