package com.muen.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SunnyWeatherApplication:Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        //设置彩云天气令牌值
        const val TOKEN="bc4IuTuHVXvMivM6"
    }

    override fun onCreate() {
        super.onCreate()
        context=applicationContext
    }

}