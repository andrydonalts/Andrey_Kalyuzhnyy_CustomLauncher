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
public class AppsManager{

    private Context context;
    private ArrayList<AppsDetail> apps;
    private ArrayList<AppsDetail> mainScreenApps;
    protected PackageManager manager;


    public AppsManager(PackageManager packageManager){
        manager = packageManager;
        apps = new ArrayList<>();
        mainScreenApps = new ArrayList<>();
    }

    public AppsManager(Context context) {
        this.context = context;
        manager = context.getPackageManager();
        apps = new ArrayList<>();
        mainScreenApps = new ArrayList<>();
    }


    public void loadApps(){


        Log.d("AppsAdapter", "In loadApps method");
        if (apps == null || apps.isEmpty()) {
            Log.d("AppsAdapter", "Loading apps");

            AppsDetail appsDetail;

            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            List<ResolveInfo> availableActives = manager.queryIntentActivities(intent, 0);

            for (int i = 0; i < availableActives.size(); i++) {
                ResolveInfo resolveInfo = availableActives.get(i);
                appsDetail = new AppsDetail();

                String label = resolveInfo.loadLabel(manager).toString();
                Drawable icon = resolveInfo.loadIcon(manager);
                String name = resolveInfo.activityInfo.packageName;

                appsDetail.setLabel(label);
                appsDetail.setName(name);
                appsDetail.setIcon(icon);
                apps.add(appsDetail);
            }
        }
    }

    public void loadMainScreenApps(){

        if (apps == null || apps.isEmpty()){
            Log.d("AppsManager", "apps == null or are empty");
            loadApps();
        }

        for (int i = 0; i < 15; i++){
            AppsDetail mainScreenApp = apps.get(i);
            mainScreenApp.setIsVisibleOnMainScreen(true);
            mainScreenApps.add(mainScreenApp);
        }
    }

    public ArrayList<AppsDetail> getAppsList(){
        return this.apps;
    }

    public void setAppsList(ArrayList<AppsDetail> apps){
        this.apps = apps;
    }

    public ArrayList<AppsDetail> getMainScreenApps(){
        return this.mainScreenApps;
    }

    public void setMainScreenApps(ArrayList<AppsDetail> mainScreenApps){
        this.mainScreenApps = mainScreenApps;
    }

    public void startApp(AppsDetail appsDetail) {
        Intent intent = manager.getLaunchIntentForPackage(appsDetail.getName());
        context.startActivity(intent);
    }

}
