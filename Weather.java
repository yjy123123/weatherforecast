package com.bignerdranch.android.weather_forecast;

import java.util.UUID;

public class Weather {
    private String weather;
    private String date;
    private String maxtemp;
    private String mintemp;
    public String picnum;
    private String cond_d;
    private String cond_n;
    private String humidity;
    private String pressure;
    private String wind;



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getMaxtemp() {
        return maxtemp;
    }

    public void setMaxtemp(String maxtemp) {
        this.maxtemp = maxtemp;
    }

    public String getMintemp() {
        return mintemp;
    }

    public void setMintemp(String mintemp) {
        this.mintemp = mintemp;
    }


    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    private UUID id;
    public Weather(){
        id=UUID.randomUUID();
    }
    public UUID getId(){
        return id;
    }



    public String getCond_d() {
        return cond_d;
    }

    public void setCond_d(String cond_d) {
        this.cond_d = cond_d;
    }

    public String getCond_n() {
        return cond_n;
    }

    public void setCond_n(String cond_n) {
        this.cond_n = cond_n;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }


    public String getPicnum() {
        return picnum;
    }

    public void setPicnum(String picnum) {
        this.picnum = picnum;
    }







}
