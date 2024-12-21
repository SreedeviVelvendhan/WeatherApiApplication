package com.sreedevi.weatherapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import coil.compose.AsyncImage
import com.sreedevi.weatherapplication.CitySelectionActivity
import com.sreedevi.weatherapplication.R
import com.sreedevi.weatherapplication.viewmodel.WeatherViewModel
import np.com.sreedevi.weatherupdate.api.NetworkResponse
import com.sreedevi.weatherapplication.model.current.WeatherModel
import com.sreedevi.weatherapplication.model.search.CurrentModelResponseItem
import com.sreedevi.weatherapplication.viewmodel.WeatherSearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import np.com.sreedevi.weatherupdate.ui.theme.HINT_COLOR
import np.com.sreedevi.weatherupdate.ui.theme.Pink40
import np.com.sreedevi.weatherupdate.ui.theme.Pink80
import np.com.sreedevi.weatherupdate.ui.theme.Purple40
import np.com.sreedevi.weatherupdate.ui.theme.SEARCHBG
import np.com.sreedevi.weatherupdate.ui.theme.SEARCHTEXTCOLOR


lateinit var dataStore : DataStore<Preferences>
lateinit var mContext :Context

class GetStringResultContract : ActivityResultContract<Unit, String?>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(context, CitySelectionActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        if (resultCode == Activity.RESULT_OK) {
            Log.d("resultCode","Activity.RESULT_OK")

            return intent?.getStringExtra("result")
        }
        return null
    }
}

val firaSansFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Light)
)
@Composable
fun WeatherPage(viewModel: WeatherViewModel, city: String) {
    mContext = LocalContext.current

    val weatherResult = viewModel.weatherResult.observeAsState()

    var result by remember { mutableStateOf("Select Location") }

    val launcher = rememberLauncherForActivityResult(GetStringResultContract()) {
        result = it ?: ""

        if(result.isNotEmpty())
            viewModel.getData(result)

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        OutlinedButton(
            onClick = {
                launcher.launch(Unit)
               // mContext.startActivity(Intent(mContext, CitySelectionActivity::class.java))
            },
            border = BorderStroke(1.dp, SEARCHBG),
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SEARCHBG)
        ){
            var showcity =""
            if (result.isEmpty()) showcity = city else showcity =result
            Text(
                text = showcity,
                //set font to the textfield
                fontFamily = firaSansFamily, fontWeight = FontWeight.Medium,
                color = HINT_COLOR,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1f)
                    .offset(x = -12.dp) //default icon width = 24.dp
            )
            Image(modifier = Modifier.padding(start = 15.dp),
                painter = painterResource(R.drawable.search_icon),
                contentDescription = null)
        }

    }

    when(val result = weatherResult.value){
            is NetworkResponse.Error -> {

                Log.d("Error: ", result.message)
            }
            NetworkResponse.Loading -> {
                //_NoSelectedCity(false)
            }
            is NetworkResponse.Success -> {
                //_NoSelectedCity(false)
                WeatherDetails(data = result.data,true)

            }
            null -> {
            }
    }
}

@Composable
fun WeatherDetails(data : WeatherModel, visible : Boolean) {
    var DetailsVisiblilty by remember { mutableStateOf(false) }
    DetailsVisiblilty = visible
    AnimatedVisibility(
        visible = DetailsVisiblilty,
        enter = fadeIn(animationSpec = tween(2000)),
        exit = fadeOut(animationSpec = tween(2000))
    ){


        Column(modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            AsyncImage(
                modifier = Modifier.size(160.dp),
                model = "https:${data.current.condition.icon}".replace("64x64","128x128"),
                contentDescription = "Condition icon"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(modifier = Modifier
                    .padding(top = 16.dp)
                    .background(Color.White),
                    textAlign = TextAlign.Center,
                    text = data.location.name,
                    fontFamily = firaSansFamily, fontWeight = FontWeight.Medium,
                    color = SEARCHTEXTCOLOR,
                    fontSize = 30.sp
                )
                Image(modifier = Modifier.padding(start = 15.dp , top = 16.dp),
                    painter = painterResource(R.drawable.vector),
                    contentDescription = null)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(text = "${data.current.temp_c}",
                    color = SEARCHTEXTCOLOR,
                    fontSize = 50.sp,
                    fontFamily = firaSansFamily, fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .background(Color.White),
                    textAlign = TextAlign.Center
                )
                Text(text = " °",
                    color = SEARCHTEXTCOLOR,
                    fontSize = 25.sp

                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
                    .clip(shape = RoundedCornerShape(16.dp))
                    .background(color = SEARCHBG),
                horizontalArrangement = Arrangement.SpaceBetween){
                Column(modifier = Modifier
                    .padding(top = 20.dp, start = 20.dp, bottom = 20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Humidity",
                        color = HINT_COLOR,
                        fontSize = 12.sp
                    )
                    Text(text ="${data.current.humidity}%",
                        //text = "sjdkj %",
                        color = HINT_COLOR,
                        fontSize = 15.sp,
                        fontFamily = firaSansFamily, fontWeight = FontWeight.Bold
                    )
                }
                Column(modifier = Modifier
                    .padding(top = 20.dp,  bottom = 20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "UV",
                        color = HINT_COLOR,
                        fontSize = 12.sp
                    )
                    Text(text = "${data.current.uv}",
                        //text = "bjh %",
                        color = HINT_COLOR,
                        fontSize = 15.sp,
                        fontFamily = firaSansFamily, fontWeight = FontWeight.Bold
                    )
                }
                Column(modifier = Modifier
                    .padding(top = 20.dp, end = 20.dp, bottom = 20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Feels Like",
                        color = HINT_COLOR,
                        fontSize = 12.sp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text(
                            fontFamily = firaSansFamily, fontWeight = FontWeight.Bold,
                            text = "${data.current.feelslike_c}",
                            color = HINT_COLOR,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "°",
                            color = HINT_COLOR,
                            fontSize = 16.sp

                        )
                    }
                }
            }
        }
    }
}

@Composable
fun _NoSelectedCity(visible:Boolean){
    Log.i("Visibility", ""+visible)
    var NoCityVisiblilty by remember { mutableStateOf(false) }
    NoCityVisiblilty = visible
    AnimatedVisibility(
        visible = NoCityVisiblilty,
        enter = fadeIn(animationSpec = tween(2000)),
        exit = fadeOut(animationSpec = tween(2000))
    ) {
        Column(modifier = Modifier
            .alpha(if (visible) 1f else 0f)
            .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Spacer(modifier = Modifier.height(170.dp))
            Text(text = "No City Selected",
                color = SEARCHTEXTCOLOR,
                fontSize = 25.sp,
                fontFamily = firaSansFamily, fontWeight = FontWeight.Bold
            )
            Text(text = "Please Search For A City",
                color = SEARCHTEXTCOLOR,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 14.dp)
            )
        }
    }
}

@Composable
fun Search_Preview(viewSearchModel: WeatherSearchViewModel) {

    var city by remember {
        mutableStateOf("")
    }
    val weatherSearch = viewSearchModel.weatherSearchResult.observeAsState()


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            //Request Textfield focus
            val focusRequester =  remember {
                FocusRequester()
            }
            val value = remember {
                mutableStateOf("")
            }
            LaunchedEffect(key1 = Unit){
                focusRequester.requestFocus()
            }
            OutlinedTextField(
                value = city,
                onValueChange = { city = it
                    viewSearchModel.getSearchData(city)},
                label = {
                    Text(
                        "Search Location",
                        color = HINT_COLOR,
                        fontFamily = firaSansFamily, fontWeight = FontWeight.Medium
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = SEARCHBG,
                    focusedIndicatorColor = SEARCHBG,
                    focusedTrailingIconColor = HINT_COLOR,
                    unfocusedContainerColor = SEARCHBG,
                    unfocusedIndicatorColor = SEARCHBG,
                    unfocusedTrailingIconColor = HINT_COLOR

                ),
                textStyle = TextStyle(fontSize = 15.sp,
                    color = SEARCHTEXTCOLOR)

            )

            IconButton(
                onClick = {
                    Log.d("Click: ","CLick")
                    viewSearchModel.getSearchData(city)
                }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search for any location"
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        when(val result = weatherSearch.value){
            is NetworkResponse.Error -> {
                Log.d("Error: ", result.message)
            }
            NetworkResponse.Loading -> {

            }
            is NetworkResponse.Success -> {
                Log.d("Search: ", result.data.toString())
                LazyColumn{
                    items(result.data){
                            data-> SearchCityItem(data, viewSearchModel)
                    }

                }
            }
            null -> {
            }
        }
    }
}

@Composable
fun SearchCityItem(currentModelResponseItem : CurrentModelResponseItem,
                   viewSearchModel: WeatherSearchViewModel){
    dataStore = mContext.createDataStore(name = "settings")
    val activity = LocalContext.current as? Activity

    Card(modifier= Modifier
        .fillMaxWidth()
        .padding(5.dp),
        shape = RoundedCornerShape(CornerSize(10.dp))

    ) {

        Row(modifier= Modifier
            .fillMaxWidth()
            .background(SEARCHBG)
            .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp)) {

            Text(text = currentModelResponseItem.name,
                color = SEARCHTEXTCOLOR,
                fontSize = 15.sp,
                fontFamily = firaSansFamily, fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier= Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
                    .background(SEARCHBG)
                    .clickable {

                        viewSearchModel.saveCityValue(currentModelResponseItem.name)

                        val intent = Intent().putExtra("result", currentModelResponseItem.name)
                        activity?.setResult(Activity.RESULT_OK, intent)
                        activity?.finish()
                    }
            )

        }
    }
}








