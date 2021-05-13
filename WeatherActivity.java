package com.bignerdranch.android.weather_forecast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;

import java.util.List;


public class WeatherActivity extends AppCompatActivity {
    private boolean notistatus;
    public static boolean ispad; //判断是否是平板
    final Handler mHandler=new Handler();
    private LocationManager locationManager;
    final  Runnable mRunnable=new Runnable() {
        @Override
        public void run() {
            FragmentManager fm=getSupportFragmentManager();
            Fragment fragment=new WeatherDetails();
            fm.beginTransaction()

                    .replace(R.id.right_fragment, fragment)
                    .commit();


        }

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);




        FragmentManager fm=getSupportFragmentManager();
        Fragment fragment;
       //
        toolbar.inflateMenu(R.menu.main_menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.settings) {
                    Intent intent=new Intent(WeatherActivity.this,SettingActivity.class);
                    startActivity(intent);
                }else {
                    if(menuItemId==R.id.maplocation){

                       maploc();




                    }else{
                        if(menuItemId==R.id.share){
                            Intent intent=new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_TEXT, MyNotification.getweatherfromdb());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(Intent.createChooser(intent, getTitle()));
                            return true;
                        }
                    }
                }
                return true;
            }
        });



        if(findViewById(R.id.right_fragment)!=null){
            ispad=true;
        }else{
            ispad=false;
        }
        Log.d("test",ispad+"");



        /*if(ispad){
            fragment=fm.findFragmentById(R.id.left_fragment);
            fragment=new WeatherListFragment();
            fm.beginTransaction()
                    .add(R.id.left_fragment, fragment)
                    .addToBackStack("")
                    .commit();
            mHandler.postDelayed(mRunnable,1000);




        }else {

            fragment = fm.findFragmentById(R.id.fragment_container);
            if (fragment == null) {
                fragment = new WeatherListFragment();
                fm.beginTransaction()

                        .add(R.id.fragment_container, fragment, "master")
                        .addToBackStack("")
                        .commit();
            }
        }*/


        MyNotification.setServiceAlarm(this,false);



    }


    public void maploc(){
        /*if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        String locationProvider = null;
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Intent i = new Intent();
            i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            getApplicationContext().startActivity(i);

        }*/

        if (!isInstalled("com.baidu.BaiduMap")) {
            return;
        }else {
            Intent i1 = new Intent();

// 展示地图

            i1.setData(Uri.parse("baidumap://map/show?src=andr.baidu.openAPIdemo"));

            startActivity(i1);
        }





    }
        private boolean isInstalled(String packageName){
            PackageManager manager = getApplicationContext().getPackageManager();
            //获取所有已安装程序的包信息
            List<PackageInfo> installedPackages = manager.getInstalledPackages(0);
            if (installedPackages != null) {
                for (PackageInfo info : installedPackages) {
                    if (info.packageName.equals(packageName))
                        return true;
                }
            }
            return false;
        }

    @Override
    public void onResume() {
        super.onResume();
        FragmentManager fm=getSupportFragmentManager();
        Fragment fragment;

        if(findViewById(R.id.right_fragment)!=null){
            ispad=true;
        }else{
            ispad=false;
        }
        Log.d("test",ispad+"");
        if(ispad){
            fragment=fm.findFragmentById(R.id.left_fragment);
            fragment=new WeatherListFragment();
            fm.beginTransaction()
                    .add(R.id.left_fragment, fragment)
                    .commit();

            mHandler.postDelayed(mRunnable,2000);

        }else {

            fragment = fm.findFragmentById(R.id.fragment_container);

                fragment = new WeatherListFragment();
                fm.beginTransaction()

                        .replace(R.id.fragment_container, fragment, "master")
                        .addToBackStack("")
                        .commit();

        }



    }
    public static Intent newIntent(Context context) {
        return new Intent(context, WeatherActivity.class);
    }
    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        notistatus=sharedPreferences.getBoolean("pre_notification",false);
        MyNotification.setServiceAlarm(this,notistatus);
    }


}
