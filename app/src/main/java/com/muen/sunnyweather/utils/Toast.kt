package com.muen.materialdesigntest.utils

import android.content.Context
import android.widget.Toast
import com.muen.sunnyweather.SunnyWeatherApplication

fun String.showToast(duration:Int=Toast.LENGTH_SHORT){
    Toast.makeText(SunnyWeatherApplication.context,this,duration).show()
}
fun Int.showToast(duration:Int=Toast.LENGTH_SHORT){
    Toast.makeText(SunnyWeatherApplication.context,this,duration).show()
}