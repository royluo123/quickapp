package com.roy.quickapp.quickapp;

import android.content.Context;

/**
 * Created by Administrator on 2017/8/11.
 */

public class ContextManager {
    private static Context sApplicationContext;
    private static Context sActivityContext;

    public static void setApplicationContext(Context context){
        sApplicationContext = context;
    }

    public static Context getApplicationContext(){
        return sApplicationContext;
    }

    public static void setActivityContext(Context context){
        sActivityContext = context;
    }

    public static Context getActivityContext(){
        return sActivityContext;
    }
}
