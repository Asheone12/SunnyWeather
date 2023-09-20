package com.muen.sunnyweather.ui.weather

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.muen.materialdesigntest.utils.showToast
import com.muen.sunnyweather.R
import com.muen.sunnyweather.databinding.ActivityWeatherBinding
import com.muen.sunnyweather.logic.model.Weather
import com.muen.sunnyweather.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.Locale


class WeatherActivity : AppCompatActivity() {
    lateinit var binding:ActivityWeatherBinding
    val viewModel by lazy{ViewModelProvider(this)[WeatherViewModel::class.java]}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityWeatherBinding.inflate(layoutInflater)
        //使状态栏与背景融合在一起
        val decorView=window.decorView
        decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor= Color.TRANSPARENT
        setContentView(binding.root)

        binding.now.navBtn.setOnClickListener{
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.drawerLayout.addDrawerListener(object :DrawerLayout.DrawerListener{
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {}
            override fun onDrawerClosed(drawerView: View) {
                //隐藏输入法
                val manager=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
            }
            override fun onDrawerStateChanged(newState: Int) {}
        })

        if(viewModel.locationLng.isEmpty()){
            viewModel.locationLng=intent.getStringExtra("location_lng")?:""
        }
        if(viewModel.locationLat.isEmpty()){
            viewModel.locationLat=intent.getStringExtra("location_lat")?:""
        }
        if(viewModel.placeName.isEmpty()){
            viewModel.placeName=intent.getStringExtra("placeName")?:""
        }
        viewModel.weatherLiveData.observe(this, Observer {result->
            val weather=result.getOrNull()
            if(weather!=null){
                showWeatherInfo(weather)
            }else{
                "无法成功获取天气信息".showToast()
                result.exceptionOrNull()?.printStackTrace()
            }
            binding.swipeRefresh.isRefreshing=false
        })
        binding.swipeRefresh.setColorSchemeResources(R.color.black)
        refreshWeather()
        binding.swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }
        viewModel.refreshWeather(viewModel.locationLng,viewModel.locationLat)
    }

    fun refreshWeather() {
        viewModel.refreshWeather(viewModel.locationLng,viewModel.locationLat)
        binding.swipeRefresh.isRefreshing=true
    }

    private fun showWeatherInfo(weather: Weather) {
        binding.now.placeName.text=viewModel.placeName
        val realTime=weather.realTime
        val daily=weather.daily
        //填充now.xml布局中的数据
        val currentTempText="${realTime.temperature.toInt()} ℃"
        binding.now.currentTemp.text=currentTempText
        binding.now.currentSky.text= getSky(realTime.skycon).info
        val currencyPM25Text="空气指数 ${realTime.airQuality.aqi.chn.toInt()}"
        binding.now.currentAQI.text=currencyPM25Text
        binding.now.nowLayout.setBackgroundResource(getSky(realTime.skycon).bg)
        //填充forecast.xml 布局中的数据
        binding.forecast.forecastLayout.removeAllViews()
        val days=daily.skycon.size
        for(i in 0 until days){
            val skycon=daily.skycon[i]
            val temperature=daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item,binding.forecast.forecastLayout,false)
            val dateInfo=view.findViewById<TextView>(R.id.dateInfo)
            val skyIcon=view.findViewById<ImageView>(R.id.skyIcon)
            val skyInfo=view.findViewById<TextView>(R.id.skyInfo)
            val temperatureInfo = view.findViewById<TextView>(R.id.temperatureInfo)
            val simpleDateFormat=SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text=simpleDateFormat.format(skycon.date)
            val sky= getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text=sky.info
            val tempText="${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text=tempText
            binding.forecast.forecastLayout.addView(view)
        }
        //填充life_index.xml布局中的数据
        val lifeIndex=daily.lifeIndex
        binding.lifeIndex.coldRiskText.text=lifeIndex.coldRisk[0].desc
        binding.lifeIndex.dressingText.text=lifeIndex.dressing[0].desc
        binding.lifeIndex.ultravioletText.text=lifeIndex.ultraviolet[0].desc
        binding.lifeIndex.carWashingText.text=lifeIndex.carWashing[0].desc
        binding.weatherLayout.visibility= View.VISIBLE
    }
}