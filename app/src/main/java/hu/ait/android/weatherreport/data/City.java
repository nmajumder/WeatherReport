package hu.ait.android.weatherreport.data;

import com.orm.SugarRecord;

/**
 * Created by nathanmajumder on 5/2/16.
 */
public class City extends SugarRecord {

    String name;
    Double temp;
    String iconID;
    String countryID;

    public City(){}

    public City(String cityName) {
        this.name = cityName;
        temp = 0.0;
        iconID = "01d";
        countryID = "--";
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public String getIconID() {
        return iconID;
    }

    public void setIconID(String iconID) {
        this.iconID = iconID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryID() {
        return countryID;
    }

    public void setCountryID(String countryID) {
        this.countryID = countryID;
    }

}
