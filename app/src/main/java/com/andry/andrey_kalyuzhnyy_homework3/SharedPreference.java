package com.andry.andrey_kalyuzhnyy_homework3;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by andry on 13.05.2016.
 */
public class SharedPreference {

    public static final String PREFS_NAME = "LAUNCHER_PREFS";
    public static final String MAIN_SCREEN_APPS = "MAIN_SCREEN_APPS";

    public SharedPreference(){
        super();
    }

    public void saveMainScreenApps(Context context, List<AppsDetail> mainScreenApps){

        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonApps = gson.toJson(mainScreenApps);
        Log.d("SharedPreference", jsonApps);
        editor.putString(MAIN_SCREEN_APPS, jsonApps);
        editor.commit();
    }

    public void addMainScreenApp(Context context, AppsDetail app){
        List<AppsDetail> mainScreenApps = getMainScreenApps(context);
        if (mainScreenApps == null) {
            mainScreenApps = new ArrayList<>();
        }
        mainScreenApps.add(app);
        saveMainScreenApps(context, mainScreenApps);
    }

    public void addMainScreenApps(Context context, List<AppsDetail> apps){
        List<AppsDetail> mainScreenApps = getMainScreenApps(context);
        if (mainScreenApps == null){
            mainScreenApps = new ArrayList<>();
            mainScreenApps.addAll(apps);
            saveMainScreenApps(context, mainScreenApps);
        }
    }

    public void deleteMainScreenApp(Context context, AppsDetail app){
        List<AppsDetail> mainScreenApps = getMainScreenApps(context);
        if (mainScreenApps != null){
            mainScreenApps.remove(app);
            saveMainScreenApps(context, mainScreenApps);
        }
    }

    public void deleteAllMainScreenApps(Context context){
        List<AppsDetail> mainScreenApps = getMainScreenApps(context);
        if (mainScreenApps != null) {
            mainScreenApps.clear();
        }
    }

    public ArrayList<AppsDetail> getMainScreenApps(Context context){

        SharedPreferences settings;
        List<AppsDetail> mainScreenApps;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (settings.contains(MAIN_SCREEN_APPS)){
            String jsonApps = settings.getString(MAIN_SCREEN_APPS, null);
            Gson gson = new Gson();
            AppsDetail[] appsArray = gson.fromJson(jsonApps, AppsDetail[].class);
            mainScreenApps = Arrays.asList(appsArray);
            //mainScreenApps = new ArrayList<AppsDetail>(mainScreenApps);
        } else {
            return null;
        }

        return  (ArrayList<AppsDetail>) mainScreenApps;
    }
}
