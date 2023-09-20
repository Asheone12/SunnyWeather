package com.muen.sunnyweather.ui.place

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.muen.materialdesigntest.utils.showToast
import com.muen.sunnyweather.MainActivity
import com.muen.sunnyweather.databinding.FragmentPlaceBinding
import com.muen.sunnyweather.ui.weather.WeatherActivity

class PlaceFragment:Fragment() {
    private lateinit var binding:FragmentPlaceBinding
    val viewModel by lazy{ ViewModelProvider(this)[PlaceViewModel::class.java] }
    private lateinit var adapter: PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentPlaceBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //对存储状态进行判断
        if(activity is MainActivity && viewModel.isPlaceSaved()){
            val place=viewModel.getSavedPlace()
            val intent= Intent(context,WeatherActivity::class.java).apply {
                putExtra("location_lng",place.location.lng)
                putExtra("location_lat",place.location.lat)
                putExtra("placeName",place.name)
            }
            startActivity(intent)
            activity?.finish()
            return
        }

        //绑定recyclerView适配器
        val layoutManager=LinearLayoutManager(activity)
        binding.recyclerView.layoutManager=layoutManager
        adapter= PlaceAdapter(this,viewModel.placeList)
        binding.recyclerView.adapter=adapter
        //创建搜索内容变化事件监听器
        binding.searchPlaceEdit.addTextChangedListener { editable->
            val content=editable.toString()
            if(content.isNotEmpty()){
               //搜索框不为空，发起搜索城市数据的请求
                viewModel.searchPlaces(content)
            }else{
                //搜索框为空，隐藏搜索结果，显示背景图片
                binding.recyclerView.visibility=View.GONE
                binding.bgImageView.visibility=View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }
        //对placeLiveData的数据变化进行观察
        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer { result->
            val places =result.getOrNull()
            if(places!=null){
                //数据不为空，显示搜索结果，并将数据添加到PlaceViewModel的placeList中
                binding.recyclerView.visibility=View.VISIBLE
                binding.bgImageView.visibility=View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            }else{
                //数据为空弹出提示并打印异常原因
                "未能查询到任何地点".showToast()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}