package com.example.a1212199_1210352_courseproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String taskTitle = intent.getStringExtra("title");
        String taskDescription = intent.getStringExtra("description");

        NotificationUtils.createNotification(context, taskTitle, taskDescription);
    }
}

