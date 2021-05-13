package com.bignerdranch.android.weather_forecast;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class network  {
    private static final String API_KEY = "";
    private String unit;
    private String location;
    private String temp_unit;
    private String wind_unit;
    private String pres_unit="hPa";
    private String hum_unit="%";
        public byte[] getUrlBytes(String urlSpec) throws IOException {
            URL url = new URL(urlSpec);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = connection.getInputStream();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new IOException(connection.getResponseMessage() +
                            ": with " +
                            urlSpec);
                }
                int bytesRead = 0;
                byte[] buffer = new byte[1024];
                while ((bytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }
                out.close();
                return out.toByteArray();
            } finally {
                connection.disconnect();
            }
        }
        public String getUrlString(String urlSpec) throws IOException {
            return new String(getUrlBytes(urlSpec));
        }

        public List<Weather> Weatheritems(){
            List<Weather> items=new ArrayList<Weather>();
            try {
                SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(WeatherLab.getContext());
                unit=sp.getString("pre_temunit","Metric");
                if(unit.equals("Metric")){
                    unit="m";
                    temp_unit="°C";
                    wind_unit="km/h";
                }else{
                    unit="i";
                    temp_unit="°F";
                    wind_unit="mile/h";
                }
                location=sp.getString("pre_location","长沙");
                String url = Uri.parse("https://free-api.heweather.net/s6/weather/forecast")
                        .buildUpon()
                        .appendQueryParameter("location", location)
                        .appendQueryParameter("unit",unit)
                        .appendQueryParameter("key", API_KEY)
                        .build().toString();
                String json=getUrlString(url);
                Log.i("test", "Received JSON: " + json);
                JSONObject jsonBody = new JSONObject(json);
                parseItems(items,jsonBody);
            }catch (Exception ex){
                Log.e("test", "Failed to fetch URL: ",ex);
            }
            return items;
        }

    private void parseItems(List<Weather> items, JSONObject jsonBody)  throws IOException, JSONException {
            database  dbhelper;
        JSONArray HeWeather= jsonBody.getJSONArray("HeWeather6");
        JSONObject msg=HeWeather.getJSONObject(0);
        JSONArray jsonArray = msg.getJSONArray("daily_forecast");
        dbhelper=new database(   WeatherLab.getContext(),"weathers.db",null,1);
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        //db.execSQL("drop table if exists weathers");//之前的数据没用了
       // dbhelper.onCreate(db);
        dbhelper.start(db);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            Weather item = new Weather();
            item.setDate(object.getString("date"));
            item.setMaxtemp(object.getString("tmp_max")+" "+temp_unit);
            item.setMintemp(object.getString("tmp_min")+" "+temp_unit);
            item.setHumidity(object.getString("hum")+" "+hum_unit);
            item.setPressure(object.getString("pres")+" "+pres_unit);
            item.setWind(object.getString("wind_spd")+" "+wind_unit);
            values.put("date",object.getString("date"));
            values.put("maxtemp",object.getString("tmp_max")+" "+temp_unit);
            values.put("mintemp",object.getString("tmp_min")+" "+temp_unit);
            values.put("picnum",object.getString("cond_code_d"));
            values.put("weather",object.getString("cond_txt_n"));
            values.put("humidity",object.getString("hum")+" "+hum_unit);
            values.put("wind",object.getString("wind_spd")+" "+wind_unit);
            values.put("pressure",object.getString("pres")+" "+pres_unit);
            db.insert("weathers",null,values);
            values.clear();
            int hour=new Time().hour;
            if(i==0) {   //当前区分一下夜晚和白天
                if (hour > 12) {
                    item.setWeather(object.getString("cond_txt_n"));
                    item.setPicnum(object.getString("cond_code_n"));
                } else {
                    item.setWeather(object.getString("cond_txt_d"));
                    item.setPicnum(object.getString("cond_code_d"));
                }
            }else{
                item.setWeather(object.getString("cond_txt_d"));
                item.setPicnum(object.getString("cond_code_d"));
            }
            items.add(item);
        }
    }
}


