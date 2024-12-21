package com.sreedevi.weatherapplication

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sreedevi.weatherapplication.R
import com.sreedevi.weatherapplication.viewmodel.WeatherSearchViewModel
import np.com.sreedevi.weatherupdate.api.NetworkResponse
import np.com.sreedevi.weatherupdate.ui.theme.HINT_COLOR
import np.com.sreedevi.weatherupdate.ui.theme.SCREEN_BG
import np.com.sreedevi.weatherupdate.ui.theme.SEARCHBG
import np.com.sreedevi.weatherupdate.ui.theme.SEARCHTEXTCOLOR

//Just to view the design
//Dummy class
@Preview
@Composable
fun WeatherPage_Preview(/*viewModel: WeatherViewModel*/) {
    
    var city by remember {
        mutableStateOf("")
    }

   // val weatherResult = viewModel.weatherResult.observeAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,) {

        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = {
                Text(
                    "Search Location",
                    color = HINT_COLOR
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },

            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = androidx.compose.material3.TextFieldDefaults.colors(
                focusedContainerColor = SEARCHBG,
                focusedIndicatorColor = SEARCHBG,
                focusedLeadingIconColor = HINT_COLOR,
                unfocusedContainerColor = SEARCHBG,
                unfocusedIndicatorColor = SEARCHBG,
                unfocusedLeadingIconColor = HINT_COLOR,
                focusedLabelColor = SEARCHTEXTCOLOR
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = "Search Location",
                color = HINT_COLOR,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
            Image(modifier = Modifier.padding(start = 15.dp),
                painter = painterResource(android.R.drawable.ic_search_category_default),
                contentDescription = null)
        }
        OutlinedButton(
            onClick = {},
            border = BorderStroke(1.dp, SEARCHBG),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SEARCHBG)
        ){

            Text(
                text = "Search Location",
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
        IconButton(modifier = Modifier.fillMaxWidth(),
            onClick = {
           // viewModel.getData(city)
            keyboardController?.hide()
        }) {
            Text(text = "Search Location",
                color = HINT_COLOR,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
            Icon(imageVector = Icons.Default.Search,
                contentDescription = "Search for any location"
            )
        }

        if(city.isEmpty())
            NoSelectedCity()

    }
   /* when(val result = weatherResult.value){
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }
            NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }
            is NetworkResponse.Success -> {
                WeatherDetails(data = result.data)
            }
            null -> {
            }
    }*/
}

@Preview
@Composable
fun WeatherDetails() {
    Column(modifier = Modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painterResource(id = R.drawable.ic_launcher_background),
            modifier = Modifier.size(160.dp),
            //model = "https:${data.current.condition.icon}".replace("64x64","128x128"),
            contentDescription = "Condition icon"
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = "Name",
                color = SEARCHTEXTCOLOR,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Image(modifier = Modifier.padding(start = 15.dp),
                painter = painterResource(R.drawable.vector),
                contentDescription = null)
        }
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
                text = "Name",
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
            Text(text = "30",
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
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = SEARCHBG),
            horizontalArrangement = Arrangement.SpaceBetween){
            Column(modifier = Modifier
                .padding(top = 20.dp, start = 20.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Humidity",
                    color = HINT_COLOR,
                    fontSize = 14.sp
                )
                Text(text ="85%",
                    //text = "sjdkj %",
                    color = HINT_COLOR,
                    fontSize = 20.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
            Column(modifier = Modifier
                .padding(top = 20.dp,  bottom = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "UV",
                    color = HINT_COLOR,
                    fontSize = 14.sp
                )
                Text(text = "50%",
                    //text = "bjh %",
                    color = HINT_COLOR,
                    fontSize = 20.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
            Column(modifier = Modifier
                .padding(top = 20.dp, end = 20.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Feels Like",
                    color = HINT_COLOR,
                    fontSize = 14.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(
                        fontFamily = firaSansFamily, fontWeight = FontWeight.Bold,
                        text = "80",
                        color = HINT_COLOR,
                        fontSize = 15.sp,
                        modifier = Modifier
                            .padding(top = 10.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(modifier = Modifier
                        .padding(top = 5.dp),
                        text = "°",
                        color = HINT_COLOR,
                        fontSize = 16.sp

                    )
                }
            }
        }
    }

}
@Composable
fun NoSelectedCity(){
    Column(modifier = Modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "No City Selected",
            color = SEARCHTEXTCOLOR,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Text(text = "Please Search For A City",
            color = SEARCHTEXTCOLOR,
            fontSize = 15.sp,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}


@Preview
@Composable
fun _Search_Preview(/*viewModel: WeatherViewModel*/) {

    var city by remember {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = {
                Text(
                    "Search Location",
                    color = HINT_COLOR
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },

            modifier = Modifier
                .fillMaxWidth(),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            colors = androidx.compose.material3.TextFieldDefaults.colors(
                focusedContainerColor = SEARCHBG,
                focusedIndicatorColor = SEARCHBG,
                focusedLeadingIconColor = HINT_COLOR,
                unfocusedContainerColor = SEARCHBG,
                unfocusedIndicatorColor = SEARCHBG,
                unfocusedLeadingIconColor = HINT_COLOR,
                focusedLabelColor = SEARCHTEXTCOLOR
            )
        )
    }
}

@Preview
@Composable
fun _SearchCityItem(){

    Card(modifier= Modifier
        .fillMaxWidth()
        .padding(5.dp),
        shape = RoundedCornerShape(CornerSize(10.dp))

    ) {
        Row(modifier= Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp)) {

            Text(text = "Test",
                color = SEARCHTEXTCOLOR,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier= Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
                    .clickable {

                    }
            )

        }
    }

}
@Preview
@Composable
fun Search_PreviewNew() {

    var city by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = {
                    Text(
                        "Search Location",
                        color = HINT_COLOR
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                colors = androidx.compose.material3.TextFieldDefaults.colors(
                    focusedContainerColor = SEARCHBG,
                    focusedIndicatorColor = SEARCHBG,
                    focusedLeadingIconColor = HINT_COLOR,
                    unfocusedContainerColor = SEARCHBG,
                    unfocusedIndicatorColor = SEARCHBG,
                    unfocusedLeadingIconColor = HINT_COLOR,
                    focusedLabelColor = SEARCHTEXTCOLOR
                )
            )
            IconButton(
                onClick = {
                    Log.d("Click: ", "CLick")

                }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search for any location"
                )
            }
        }
        /* Row(
             modifier = Modifier
                 .fillMaxWidth()
                 .padding(16.dp)
                 .clip(shape = RoundedCornerShape(16.dp))
                 .background(color = SEARCHBG)
         ){



         }*/
    }
}











