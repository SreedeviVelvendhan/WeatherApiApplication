package com.sreedevi.weatherapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sreedevi.weatherapplication.viewmodel.WeatherViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import np.com.sreedevi.weatherupdate.ui.theme.WeatherUpdateTheme

class MainActivity : ComponentActivity() {
    lateinit var dataStore : DataStore<Preferences>
     var savedcity :String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        dataStore = createDataStore(name = "settings")
        //getCityValue("city")
        GlobalScope.launch {
            var value = read("city")
            value?.let {
                savedcity = value
            }/*
            if(value!=null)
            savedcity = value!!*/
            Log.i("MainActivity:",value+" $savedcity")

        }
        setContent {
            WeatherUpdateTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {

                    if(savedcity.isNotEmpty()) weatherViewModel.getData(savedcity) else _NoSelectedCity(true)
                    WeatherPage(weatherViewModel, savedcity)
                }
            }
        }
    }
    private suspend fun read(key: String) :String?{
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }
}

