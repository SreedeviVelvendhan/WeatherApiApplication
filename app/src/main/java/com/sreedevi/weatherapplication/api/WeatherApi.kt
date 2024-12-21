package np.com.sreedevi.weatherupdate.api

import com.sreedevi.weatherapplication.model.current.WeatherModel
import com.sreedevi.weatherapplication.model.search.CurrentModelResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {


    @GET("/v1/current.json")
    suspend fun getWeather(
        @Query("key") apikey : String,
        @Query("q") city : String
    ) : Response<WeatherModel>

    @GET("/v1/search.json")
    suspend fun getSearchWeather(
        @Query("key") apikey : String,
        @Query("q") city : String
    ) : Response<CurrentModelResponse>

}