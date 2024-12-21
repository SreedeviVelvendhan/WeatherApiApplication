package com.sreedevi.weatherapplication.viewmodel

import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sreedevi.weatherapplication.dataStore
import kotlinx.coroutines.launch
import com.sreedevi.weatherapplication.model.current.Constant
import np.com.sreedevi.weatherupdate.api.NetworkResponse
import np.com.sreedevi.weatherupdate.api.RetrofitInstance
import com.sreedevi.weatherapplication.model.current.WeatherModel
import kotlinx.coroutines.flow.first

class WeatherViewModel :ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    val cityvalue = MutableLiveData<String>()

    fun getData(city : String){
        _weatherResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try{
                val response = weatherApi.getWeather(Constant.apiKey,city)
                if(response.isSuccessful){
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                }else{
                    _weatherResult.value = NetworkResponse.Error("Failed to load data")
                }
            }
            catch (e : Exception){
                _weatherResult.value = NetworkResponse.Error("Failed to load data")
            }

        }
    }



}












