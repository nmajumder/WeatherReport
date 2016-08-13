package hu.ait.android.weatherreport.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hu.ait.android.weatherreport.CityListActivity;
import hu.ait.android.weatherreport.WeatherDetailsActivity;
import hu.ait.android.weatherreport.data.City;
import hu.ait.android.weatherreport.R;
import hu.ait.android.weatherreport.data.WeatherData;
import hu.ait.android.weatherreport.network.WeatherAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nathanmajumder on 5/2/16.
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder>
    implements CityTouchHelperAdapter {

    private Context context;
    private List<City> cities = new ArrayList<City>();

    public CityAdapter(Context context) {
        this.context = context;

        cities = City.listAll(City.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_row, parent, false);

        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvCityName.setText(cities.get(position).getName()+", "+cities.get(position).getCountryID());
        holder.tvCurrentTemp.setText(cities.get(position).getTemp().toString()+"°F");
        String iconID = cities.get(position).getIconID();
        switch(iconID) {
            case "01d": holder.weatherIcon.setImageResource(R.drawable.img01d); break;
            case "01n": holder.weatherIcon.setImageResource(R.drawable.img01n); break;
            case "02d": holder.weatherIcon.setImageResource(R.drawable.img02d); break;
            case "02n": holder.weatherIcon.setImageResource(R.drawable.img02n); break;
            case "03d": holder.weatherIcon.setImageResource(R.drawable.img03d); break;
            case "03n": holder.weatherIcon.setImageResource(R.drawable.img03n); break;
            case "04d": holder.weatherIcon.setImageResource(R.drawable.img04d); break;
            case "04n": holder.weatherIcon.setImageResource(R.drawable.img04n); break;
            case "09d": holder.weatherIcon.setImageResource(R.drawable.img09d); break;
            case "09n": holder.weatherIcon.setImageResource(R.drawable.img09n); break;
            case "10d": holder.weatherIcon.setImageResource(R.drawable.img10d); break;
            case "10n": holder.weatherIcon.setImageResource(R.drawable.img10n); break;
            case "11d": holder.weatherIcon.setImageResource(R.drawable.img11d); break;
            case "11n": holder.weatherIcon.setImageResource(R.drawable.img11n); break;
            case "13d": holder.weatherIcon.setImageResource(R.drawable.img13d); break;
            case "13n": holder.weatherIcon.setImageResource(R.drawable.img13n); break;
            case "50d": holder.weatherIcon.setImageResource(R.drawable.img50d); break;
            case "50n": holder.weatherIcon.setImageResource(R.drawable.img50n); break;
            default: holder.weatherIcon.setImageResource(R.drawable.img01d); break;
        }
        holder.btnDelete.setBackgroundResource(R.drawable.delete);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCity(position);
            }
        });
        holder.rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                City city = cities.get(position);
                Intent showDetailsIntent = new Intent(context, WeatherDetailsActivity.class);
                showDetailsIntent.putExtra(WeatherDetailsActivity.KEY_CITY_NAME, city.getName());
                context.startActivity(showDetailsIntent);
            }
        });
    }

    public void updateAllCities() {
        for (int i = 0; i < cities.size(); i++) {
            City city = cities.get(i);
            updateCityWeather(city, i);
        }
        notifyDataSetChanged();

        CityListActivity.btnUpdate.setBackgroundColor(Color.parseColor("#888888"));
        CityListActivity.btnUpdate.setText(R.string.updateBtnString);
    }

    private void updateCityWeather(City city, final int position) {
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(context.getResources().getString(R.string.openWeatherBaseURL)).
                addConverterFactory(GsonConverterFactory.create()).
                build();

        final WeatherAPI weatherAPI = retrofit.create(WeatherAPI.class);

        // this is where we pass in the city name and API key
        Call<WeatherData> weatherQuery = weatherAPI.getCurrentWeather(city.getName(),
                context.getResources().getString(R.string.imperialUnitsTag),
                context.getResources().getString(R.string.ApiKey));

        weatherQuery.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.body().getBase() == null) {
                    Toast.makeText(context, "City name not found", Toast.LENGTH_LONG).show();
                    removeCity(position);
                } else {
                    updateCity(response.body().getName(),
                            response.body().getMain().getTemp(),
                            response.body().getWeather().get(0).getIcon(),
                            response.body().getSys().getCountry(),
                            position);
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                t.getMessage();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public void addCity(City city) {
        cities.add(0, city);
        city.save();

        updateCityWeather(city,0);

        notifyDataSetChanged();
    }

    public void updateCity(String name, double temp, String iconID, String country, int position) {
        City city = cities.get(position);
        city.setName(name);
        city.setTemp(temp);
        city.setIconID(iconID);
        city.setCountryID(country);
        city.save();

        notifyDataSetChanged();
    }

    public void removeCity(int position) {
        City.delete(cities.get(position));
        cities.remove(position);

        notifyDataSetChanged();
    }

    public void removeAllCities() {
        for (City city : cities) {
            City.delete(city);
        }
        cities.clear();

        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onItemDismiss(int position) {
        City.delete(cities.get(position));
        cities.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(cities, i, i + 1);
                }
        } else {
                for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(cities, i, i - 1);
                }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCityName;
        private Button btnDelete;
        private TextView tvCurrentTemp;
        private ImageView weatherIcon;
        private LinearLayout rowLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            rowLayout = (LinearLayout) itemView.findViewById(R.id.rowLayout);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            tvCityName = (TextView) itemView.findViewById(R.id.tvCityName);
            tvCurrentTemp = (TextView) itemView.findViewById(R.id.rowCurrentTemp);
            weatherIcon = (ImageView) itemView.findViewById(R.id.rowWeatherIcon);

        }
    }

}
