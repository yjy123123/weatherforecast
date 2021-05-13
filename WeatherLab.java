package com.bignerdranch.android.weather_forecast;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WeatherLab extends Application {
    private static Context mContext;
    private List<Weather> mWeathers=new ArrayList<>();
    public Weather focus;

    public void onCreate(){
        super.onCreate();
        mContext = getApplicationContext();
    }
    public static Context getContext(){
        return mContext;
    }


    public Weather getFocus() {
        return focus;
    }

    public void setFocus(Weather focus) {
        this.focus = focus;
    }


    public   List<Weather> getWeathers(){
        return mWeathers;
    }


    public Weather getWeather(UUID id){
        for(Weather weather:mWeathers){
            if(weather.getId().equals(id)){
                return weather;
            }
        }
                return null;
    }
    public void add(Weather weather){
        mWeathers.add(weather);
    }

}
