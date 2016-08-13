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

import hu.ait.android.weatherreport.R;
import hu.ait.android.weatherreport.data.WeatherData;

/**
 * Created by nathanmajumder on 5/4/16.
 */
public class ShowSummaryFragment extends Fragment {

    TextView tvCurrentTemp;
    ImageView currentWeatherIcon;
    TextView tvDescrip;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_show_summary, null);

        tvCurrentTemp = (TextView) rootView.findViewById(R.id.tvCurrentTemp);
        tvDescrip = (TextView) rootView.findViewById(R.id.tvDescrip);
        currentWeatherIcon = (ImageView) rootView.findViewById(R.id.currentWeatherIcon);
        currentWeatherIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);

        EventBus.getDefault().register(this);

        return rootView;
    }

    @Subscribe
    public void onEvent(WeatherData result) {

        String imgID = result.getWeather().get(0).getIcon();

        tvCurrentTemp.setText(result.getMain().getTemp().toString() + getResources().getString(R.string.degFahrExtension));
        tvDescrip.setText(result.getWeather().get(0).getDescription());

        Glide.with(this).load(getResources().getString(R.string.weatherIconWebString)+imgID
                        +getResources().getString(R.string.pngExtension)).into(currentWeatherIcon);

    }
}
