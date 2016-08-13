package hu.ait.android.weatherreport.network;

import hu.ait.android.weatherreport.data.WeatherData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by nathanmajumder on 5/8/16.
 */
public interface WeatherAPI {
    @GET("weather")
    Call<WeatherData> getCurrentWeather(@Query("q") String base, @Query("units") String key,
                                        @Query("appid") String appid);
}
