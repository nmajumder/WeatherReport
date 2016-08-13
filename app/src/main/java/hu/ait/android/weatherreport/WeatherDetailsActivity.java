package hu.ait.android.weatherreport;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import hu.ait.android.weatherreport.adapter.PagerAdapter;
import hu.ait.android.weatherreport.data.WeatherData;
import hu.ait.android.weatherreport.network.WeatherAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherDetailsActivity extends AppCompatActivity {

    public static final String KEY_CITY_NAME = "cityName";

    Button btnBack;

    private String cityName;
    TextView tvCityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);

        PagerAdapter adapter = new PagerAdapter(this,getSupportFragmentManager());

        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);

        pager.setAdapter(adapter);


        tvCityName = (TextView) findViewById(R.id.tvCityNameDetails);
        cityName = getIntent().getStringExtra(KEY_CITY_NAME);
        tvCityName.setText(cityName);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateWeather(cityName);
    }

    public void updateWeather(String city) {
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(getString(R.string.openWeatherBaseURL)).// this is the open weather base url
                addConverterFactory(GsonConverterFactory.create()).
                build();

        final WeatherAPI weatherAPI = retrofit.create(WeatherAPI.class);

        // this is where we pass in the city name and API key
        Call<WeatherData> weatherQuery = weatherAPI.getCurrentWeather(city,
                getString(R.string.imperialUnitsTag),
                getString(R.string.ApiKey));

        weatherQuery.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                tvCityName.setText(response.body().getName().toString()+", "+
                                    response.body().getSys().getCountry());
                EventBus.getDefault().post(response.body());
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                t.getMessage();
            }
        });
    }
}
