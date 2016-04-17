package com.andry.andrey_kalyuzhnyy_homework3;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andry on 16.04.2016.
 */
public class Adapter extends BaseAdapter {

    private Context context;
    private ArrayList<AppsDetail> apps;
    private PackageManager manager;

    public Adapter(Context context) {
        this.context = context;
        this.apps = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public Object getItem(int position) {
        return apps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;

        if (convertView == null){
            holder = new Holder();
            convertView = View.inflate(context, R.layout.app_item, null);

            holder.icon = (ImageView) convertView.findViewById(R.id.app_item_icon);
            holder.label = (TextView) convertView.findViewById(R.id.app_item_label);
            holder.item = convertView.findViewById(R.id.app_item_item);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();

        }

        final AppsDetail appsDetail = apps.get(position);
        holder.icon.setImageDrawable(appsDetail.getIcon());
        holder.label.setText(appsDetail.getLabel());

        // opening app
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = manager.getLaunchIntentForPackage(appsDetail.getName());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    // getting list of all apps
    public void loadApps(){
        manager = context.getPackageManager();
        AppsDetail appsDetail;

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActives = manager.queryIntentActivities(intent, 0);
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
    }

    private class Holder{
        ImageView icon;
        TextView label;
        View item;
    }

}
