package com.rahuljanagouda.popularmoviestwo.database;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by rahuljanagouda on 25/08/16.
 */
public class DatabaseChangedReceiver extends BroadcastReceiver {

    @NonNull
    public static final String ACTION_DATABASE_CHANGED = "com.rahuljanagouda.popularmoviestwo";

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
