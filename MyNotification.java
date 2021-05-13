package com.bignerdranch.android.weather_forecast;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyNotification extends IntentService {
    public MyNotification() {
        super("MyNotification");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("Notification", "Received an intent: " + intent);
       // Resources resources = getResources();
        Intent i = WeatherActivity.newIntent(this);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);// 点击事件
        Notification notification = new NotificationCompat.Builder(this,"mynotification")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Weatherforecast")
                .setContentText(getweatherfromdb())
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);
        notificationManager.notify(0, notification);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, MyNotification.class);
    }

    public static String getweatherfromdb(){
        database dbhelper = new database(WeatherLab.getContext(), "weathers.db", null, 1);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = db.query("weathers", null, null, null, null, null, null);
        String msg="";
        if(cursor.moveToFirst()){
            msg="Forceast:"+cursor.getString(cursor.getColumnIndex("weather"))+
                    " high:" +cursor.getString(cursor.getColumnIndex("maxtemp"))
                    +" low:"+cursor.getString(cursor.getColumnIndex("mintemp"));
        }
        Log.d("test",msg);
        return msg;
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = MyNotification.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), TimeUnit.MINUTES.toMillis(1), pi);

        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        } }

    public static boolean isServiceAlarmOn(Context context) {
        Intent i =MyNotification.newIntent(context);
        PendingIntent pi = PendingIntent
                .getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }
}
