package br.com.devmker.firebaseteste;

import android.app.Application;

import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by DevMaker on 5/20/16.
 */
public class MyAplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseMessaging.getInstance().subscribeToTopic("general");
    }
}
