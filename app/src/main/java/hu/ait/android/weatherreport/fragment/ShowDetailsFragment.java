package hu.ait.android.weatherreport.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.Subscribe;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;

import hu.ait.android.weatherreport.R;
import hu.ait.android.weatherreport.data.WeatherData;

/**
 * Created by nathanmajumder on 5/4/16.
 */
public class ShowDetailsFragment extends Fragment {

    TextView tvCurrTemp;
    TextView tvHumidity;
    TextView tvPressure;
    TextView tvWindSpeed;
    TextView tvDate;
    TextView tvDescrip;
    ImageView currWeatherIcon;
    TextView tvSunrise;
    TextView tvSunset;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_show_details, null);

        tvCurrTemp = (TextView) rootView.findViewById(R.id.tvCurrTempDetails);
        tvHumidity = (TextView) rootView.findViewById(R.id.tvHumidity);
        tvPressure = (TextView) rootView.findViewById(R.id.tvPressure);
        tvWindSpeed = (TextView) rootView.findViewById(R.id.tvWindSpeed);
        tvDate = (TextView) rootView.findViewById(R.id.tvDate);
        tvDescrip = (TextView) rootView.findViewById(R.id.tvDescripDetails);
        tvSunrise = (TextView) rootView.findViewById(R.id.tvSunriseTime);
        tvSunset = (TextView) rootView.findViewById(R.id.tvSunsetTime);
        currWeatherIcon = (ImageView) rootView.findViewById(R.id.weatherIconDetails);

        EventBus.getDefault().register(this);

        return rootView;
    }

    @Subscribe
    public void onEvent(WeatherData result) {
        tvCurrTemp.setText(result.getMain().getTemp().toString() + getResources().getString(R.string.degFahrExtension));
        tvHumidity.setText(result.getMain().getHumidity().toString() + "%");
        tvPressure.setText(result.getMain().getPressure().toString() + getResources().getString(R.string.pressureUnits));
        tvWindSpeed.setText(result.getWind().getSpeed().toString() + getResources().getString(R.string.mph));
        tvDescrip.setText(result.getWeather().get(0).getDescription());
        tvDate.setText(DateFormat.getDateTimeInstance().format(result.getDt() * (long) 1000));
        tvSunrise.setText(DateFormat.getTimeInstance().format(result.getSys().getSunrise() * (long) 1000));
        tvSunset.setText(DateFormat.getTimeInstance().format(result.getSys().getSunset() * (long) 1000));

        String iconID = result.getWeather().get(0).getIcon();
        Glide.with(this).load(getResources().getString(R.string.weatherIconWebString)+iconID
                +getResources().getString(R.string.pngExtension)).into(currWeatherIcon);

    }
}
