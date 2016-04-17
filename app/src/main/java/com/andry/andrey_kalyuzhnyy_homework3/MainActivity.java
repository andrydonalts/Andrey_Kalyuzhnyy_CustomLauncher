package com.andry.andrey_kalyuzhnyy_homework3;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Adapter adapter;
    private GridView gridView;
    private ArrayList<AppsDetail> appsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridview);
        appsList = new ArrayList<AppsDetail>();

        adapter = new Adapter(this);
        adapter.loadApps();
        gridView.setAdapter(adapter);

        setScreenOrientation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, OtherActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setScreenOrientation(){
        Resources resources = getResources();
        if(resources.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            gridView.setNumColumns(resources.getInteger(R.integer.portrait_column_number));
        else if (resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            gridView.setNumColumns(resources.getInteger(R.integer.landscape_column_number));

    }
}
