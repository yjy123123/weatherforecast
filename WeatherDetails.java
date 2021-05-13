package com.bignerdranch.android.weather_forecast;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherDetails extends Fragment {
    private Weather mWeather;
    private WeatherLab mWeatherLab;
    //private static final String weather_id="weather_id";
    //private TextView mday;
    private TextView mdate;
    private TextView mmaxtmp;
    private TextView mmintmp;
    private TextView mhumidity;
    private TextView mpressure;
    private TextView mwind;
    private ImageView mpic;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.details_fragment,container,false);
        mWeatherLab=(WeatherLab) getActivity().getApplication();
        mWeather=mWeatherLab.getFocus();
        //mday=v.findViewById(R.id.detail_day);
        mdate=v.findViewById(R.id.detail_date);
        mmaxtmp=v.findViewById(R.id.detail_maxtemp);
        mmintmp=v.findViewById(R.id.detail_mintemp);
        mhumidity=v.findViewById(R.id.humidity);
        mpressure=v.findViewById(R.id.pressure);
        mwind=v.findViewById(R.id.wind);
        mpic=v.findViewById(R.id.detail_pic);
        updateUI();
        return v;
    }
    private void updateUI(){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        Log.d("test",mWeather.getPicnum());
        String  picture = Environment.getExternalStorageDirectory()+"/cond-icon-heweather/"+mWeather.getPicnum()+".png";
        //Uri filepath = Uri.fromFile(picture);
        Bitmap bitmap = BitmapFactory.decodeFile(picture);
        mdate.setText(mWeather.getDate());
        mmaxtmp.setText(mWeather.getMaxtemp());
        mmintmp.setText(mWeather.getMintemp());
        mhumidity.setText("Humidity: "+mWeather.getHumidity());
        mpressure.setText("Pressure: "+mWeather.getPressure());
        mwind.setText("Wind: "+mWeather.getWind());
        mpic.setImageBitmap(bitmap);
    }
   /* public static WeatherDetails newInstance(UUID weatherid){
        Bundle args=new Bundle();
        args.putSerializable(weather_id,weatherid);
        WeatherDetails fragment=new WeatherDetails();
        fragment.setArguments(args);
        return fragment;
    }*/

}
