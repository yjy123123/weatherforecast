package com.bignerdranch.android.weather_forecast;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;




public class WeatherListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private WeatherAdapter mAdapter;
    public static final String EXTRA_ID =
            "com.bignerdranch.android.weather_forecast.extra_id";
     WeatherLab mWeatherLab;
     List<Weather> mWeathers=new ArrayList<>();
     Weather focus;
    final Handler mHandler=new Handler();
    final  Runnable mRunnable=new Runnable() {
        @Override
        public void run() {
            updateUI();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setRetainInstance(true);
        loadwether();
    }


    public void loadwether(){
        if(isNetworkAvailableAndConnected()){
            new GetWeather().execute();
        }else{
            new GetWeatherfromdb().execute();
        }
    }
    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) WeatherLab.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_weather_list,container,false);
        mRecyclerView=(RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mWeatherLab=(WeatherLab) getActivity().getApplication();
        mWeathers=mWeatherLab.getWeathers();
        updateUI();
        return view;
    }

    private class WeatherHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView mImageView;
        private TextView mDateTextView;
        private TextView mWeatherTextView;
        private TextView mMaxtempTextView;
        private TextView mMintempTextView;
        private Weather mWeather;

        public WeatherHolder(LayoutInflater inflater, ViewGroup parent){

            super(inflater.inflate(R.layout.list_item_weather, parent, false));
            itemView.setOnClickListener(this);
            mImageView=itemView.findViewById(R.id.weather_image);
            mDateTextView=itemView.findViewById(R.id.date);
            mWeatherTextView=itemView.findViewById(R.id.weather);
            mMaxtempTextView=itemView.findViewById(R.id.maxtemp);
            mMintempTextView=itemView.findViewById(R.id.mintemp);
        }
        public void bind(Weather weather){
             mWeather=weather;
             //插入图片
             mDateTextView.setText(mWeather.getDate());
             mWeatherTextView.setText(mWeather.getWeather());
             mMaxtempTextView.setText(mWeather.getMaxtemp());
             mMintempTextView.setText(mWeather.getMintemp());
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
            String  picture = Environment.getExternalStorageDirectory()+"/cond-icon-heweather/"+mWeather.picnum+".png";
            //Uri filepath = Uri.fromFile(picture);
            Bitmap bitmap = BitmapFactory.decodeFile(picture);
             mImageView.setImageBitmap(bitmap);
           // mImageView.getDrawable().setLevel(Integer.parseInt(mWeather.picnum));//
        }
        @Override
        public void onClick(View view){
            if(WeatherActivity.ispad){
                mWeatherLab.setFocus(mWeather);
                FragmentManager fm = getFragmentManager();
                Fragment fragment = new WeatherDetails();
                fm.beginTransaction()
                        .replace(R.id.right_fragment, fragment)
                        .commit();
            }else {
                mWeatherLab.setFocus(mWeather);
                FragmentManager fm = getFragmentManager();
                Fragment fragment = new WeatherDetails();
                Fragment master = fm.findFragmentByTag("master");
                fm.beginTransaction()
                        .hide(master)
                        .add(R.id.fragment_container, fragment, "details")
                        .addToBackStack("")
                        .commit();
            }
        }
    }

    private class WeatherAdapter extends RecyclerView.Adapter<WeatherHolder> {
        private List<Weather> Weathers;
        public WeatherAdapter(List<Weather> weathers){
            Weathers=weathers;
        }
        @NonNull
        @Override
        public WeatherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater
                 layoutInflater = LayoutInflater.from(getActivity());
            return new WeatherHolder(layoutInflater,parent);
        }
        @Override
        public void onBindViewHolder(@NonNull WeatherHolder holder, int position) {
            Weather weather=Weathers.get(position);
            holder.bind(weather);
        }

        @Override
        public int getItemCount() {
            return mWeathers.size();
        }
    }
    private void updateUI(){
       // WeatherLab weatherLab=WeatherLab.get(getActivity());
        List<Weather> weathers=mWeathers;
        Log.d("test",mWeathers.size()+"!");
        mAdapter=new WeatherAdapter(weathers);
        mRecyclerView.setAdapter(mAdapter);
    }



    private  class GetWeather  extends AsyncTask<Void,Void,List<Weather>> {
        @Override
        protected List<Weather> doInBackground(Void... params) {
            return  new network().Weatheritems();
        }
        protected void onPostExecute(List<Weather> items) {
            mWeathers = items;
            mWeatherLab.setFocus(mWeathers.get(0));
            updateUI();
        }
    }
    private class GetWeatherfromdb extends AsyncTask<Void,Void,List<Weather>>  {
        protected List<Weather> doInBackground(Void... params) {
                database dbhelper = new database(WeatherLab.getContext(), "weathers.db", null, 1);
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                Cursor cursor = db.query("weathers", null, null, null, null, null, null);
                List<Weather> items=new ArrayList<>();
                if (cursor.moveToFirst()) {
                    do {// 遍历Cursor对象，取出数据并打印
                        Weather weather=new Weather();
                        weather.setDate(cursor.getString(cursor.getColumnIndex("date")));
                        weather.setMaxtemp(cursor.getString(cursor.getColumnIndex("maxtemp")));
                        weather.setMintemp(cursor.getString(cursor.getColumnIndex("mintemp")));
                        weather.setPicnum(cursor.getString(cursor.getColumnIndex("picnum")));
                        weather.setWeather(cursor.getString(cursor.getColumnIndex("weather")));
                        weather.setHumidity(cursor.getString(cursor.getColumnIndex("humidity")));
                        weather.setWind(cursor.getString(cursor.getColumnIndex("wind")));
                        weather.setPressure(cursor.getString(cursor.getColumnIndex("pressure")));
                        items.add(weather);
                        Log.d("test","success");
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return items;
        }
        protected void onPostExecute(List<Weather> items) {
            mWeathers = items;
            mWeatherLab.setFocus(mWeathers.get(0));
            updateUI();
        }
    }
}
