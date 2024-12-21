package com.sreedevi.weatherapplication.viewmodel

import androidx.datastore.preferences.core.edit
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
import com.sreedevi.weatherapplication.model.search.CurrentModelResponse

class WeatherSearchViewModel :ViewModel() {

    private val weatherSearchApi = RetrofitInstance.weatherApi
    private val _weatherSearchResult = MutableLiveData<NetworkResponse<CurrentModelResponse>>()
    val weatherSearchResult : LiveData<NetworkResponse<CurrentModelResponse>> = _weatherSearchResult

    var cityValue = MutableLiveData<String>()

    fun getSearchData(city : String){
        _weatherSearchResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try{
                val response = weatherSearchApi.getSearchWeather(Constant.apiKey,city)
                if(response.isSuccessful){
                    response.body()?.let {
                        _weatherSearchResult.value = NetworkResponse.Success(it)
                    }
                }else{
                    _weatherSearchResult.value = NetworkResponse.Error("Failed to load data")
                }
            }
            catch (e : Exception){
                _weatherSearchResult.value = NetworkResponse.Error("Failed to load data")
            }

        }
    }

    fun saveCityValue(city : String){
        viewModelScope.launch {
        savecity("city",city)
        }
    }
    private suspend fun savecity(key: String, value: String){
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit { settings->
            settings[dataStoreKey] =value
        }
    }
}












