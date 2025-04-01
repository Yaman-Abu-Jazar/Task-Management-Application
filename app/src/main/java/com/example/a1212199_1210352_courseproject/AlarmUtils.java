package com.example.a1212199_1210352_courseproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;

public class AlarmUtils {

    public static void setNotificationAlarm(Context context, int hour, int minute, String taskTitle, String taskDescription) {
        // Get the current time and set the alarm time
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Create an Intent that will trigger the BroadcastReceiver
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("title", taskTitle);
        intent.putExtra("description", taskDescription);

        int requestCode = (int) System.currentTimeMillis();

        // Create a PendingIntent to wrap the Intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE); // Add FLAG_IMMUTABLE here

        // Get the AlarmManager system service
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Set a one-time alarm to trigger at the specified time
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}

