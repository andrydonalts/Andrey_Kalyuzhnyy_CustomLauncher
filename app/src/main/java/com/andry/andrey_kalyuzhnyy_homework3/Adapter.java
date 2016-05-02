package com.andry.andrey_kalyuzhnyy_homework3;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by andry on 16.04.2016.
 */
public class Adapter extends BaseAdapter implements Filterable {

    public static final String APP_DETAIL_EXTRA = "APP_DETAIL_EXTRA";

    private Context context;
    private ArrayList<AppsDetail> apps;
    private ArrayList<AppsDetail> filteredApps;
    private CustomFilter filter;
    private AppsManager appsManager;

    public Adapter(Context context) {
        this.context = context;

        appsManager = new AppsManager(context);
        appsManager.loadApps();
        apps = appsManager.getAppsList();
        this.filteredApps = apps;
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
                appsManager.startApp(appsDetail);
            }
        });

        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (context instanceof AllAppsActivity) {
                    Log.d("Adapter OnLongClick", "long click");
                    AllAppsActivity activity = (AllAppsActivity) context;
                    FragmentTransaction transaction = activity.getFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null);

                    AppDeleteFragmentDialog dialog = new AppDeleteFragmentDialog();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(APP_DETAIL_EXTRA, appsDetail);
                    dialog.setArguments(bundle);
                    dialog.show(transaction, null);
                }
                return false;
            }
        });

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter();
        }
        return filter;
    }

    private class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {

                ArrayList<AppsDetail> filters = new ArrayList<>();

                for(int i = 0; i < filteredApps.size(); i++) {
                    if (filteredApps.get(i).getLabel().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        AppsDetail filteredApp = filteredApps.get(i);
                        AppsDetail appDetail = new AppsDetail(filteredApp.getLabel(), filteredApp.getName(), filteredApp.getIcon(), filteredApp.isOnMainScreen());
                        filters.add(appDetail);
                    }
                }
                results.count = filters.size();
                results.values = filters;
            } else {
                results.count = filteredApps.size();
                results.values = filteredApps;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            apps = (ArrayList<AppsDetail>) results.values;
            notifyDataSetChanged();
        }
    }


    private class Holder{
        ImageView icon;
        TextView label;
        View item;
    }



}
