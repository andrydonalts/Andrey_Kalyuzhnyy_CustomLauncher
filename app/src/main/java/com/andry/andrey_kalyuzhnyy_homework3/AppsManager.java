package com.andry.andrey_kalyuzhnyy_homework3;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andry on 26.04.2016.
 */
public class AppsManager {

    private Context context;
    private ArrayList<AppsDetail> apps;
    private ArrayList<AppsDetail> mainScreenApps;
    private PackageManager manager;

    public AppsManager(Context context) {
        this.context = context;
        manager = context.getPackageManager();
        apps = new ArrayList<>();
        mainScreenApps = new ArrayList<>();
    }

    public void loadApps(){
        AppsDetail appsDetail;

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActives = manager.queryIntentActivities(intent, 0);

        for (int i = 0; i < availableActives.size(); i++){
            ResolveInfo resolveInfo = availableActives.get(i);
            appsDetail = new AppsDetail();

            if (i < 15){
                appsDetail.setOnMainScreen(true);
            }

            String label = resolveInfo.loadLabel(manager).toString();
            Drawable icon = resolveInfo.loadIcon(manager);
            String name = resolveInfo.activityInfo.packageName;

            appsDetail.setLabel(label);
            appsDetail.setName(name);
            appsDetail.setIcon(icon);
            apps.add(appsDetail);
        }


        /*
        for (ResolveInfo resolveInfo : availableActives){
            appsDetail = new AppsDetail();


            String label = resolveInfo.loadLabel(manager).toString();
            Drawable icon = resolveInfo.loadIcon(manager);
            String name = resolveInfo.activityInfo.packageName;

            appsDetail.setLabel(label);
            appsDetail.setName(name);
            appsDetail.setIcon(icon);
            apps.add(appsDetail);
        }
        */
    }



    public void loadMainScreenApps(){
        //AppsDetail appsDetail;
        loadApps();

        Log.d("AppsManager", Integer.toString(apps.size()));

        for (AppsDetail appsDetail1 : apps){
            if (appsDetail1.isOnMainScreen() == true) {
                mainScreenApps.add(appsDetail1);
            }
        }

        Log.d("AppsManager", "onMainScreen: " + Integer.toString(mainScreenApps.size()));
        /*
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActives = manager.queryIntentActivities(intent, 0);
        int counter = 0;
            for (ResolveInfo resolveInfo : availableActives){
                if (counter == 15) {
                    return;
                }
                appsDetail = new AppsDetail();

                String label = resolveInfo.loadLabel(manager).toString();
                Drawable icon = resolveInfo.loadIcon(manager);
                String name = resolveInfo.activityInfo.packageName;

                Log.d("AppsManager", label);

                appsDetail.setLabel(label);
                appsDetail.setName(name);
                appsDetail.setIcon(icon);
                mainScreenApps.add(appsDetail);
                counter++;
            }
            */
    }


    public ArrayList<AppsDetail> getAppsList(){
        return this.apps;
    }

    public ArrayList<AppsDetail> getMainScreenApps(){
        return this.mainScreenApps;
    }

    public void startApp(AppsDetail appsDetail) {
        Intent intent = manager.getLaunchIntentForPackage(appsDetail.getName());
        context.startActivity(intent);
    }


}
