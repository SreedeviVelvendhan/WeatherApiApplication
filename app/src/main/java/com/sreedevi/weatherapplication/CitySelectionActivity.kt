package com.sreedevi.weatherapplication

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.sreedevi.weatherapplication.viewmodel.WeatherSearchViewModel
import np.com.sreedevi.weatherupdate.ui.theme.WeatherUpdateTheme

import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.Color

class CitySelectionActivity : ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val weatherSearchViewModel = ViewModelProvider(this)[WeatherSearchViewModel::class.java]


        setContent {
            WeatherUpdateTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                   Search_Preview(weatherSearchViewModel)
                }
            }
        }
    }

}