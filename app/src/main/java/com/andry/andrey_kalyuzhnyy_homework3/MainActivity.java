package com.andry.andrey_kalyuzhnyy_homework3;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Adapter adapter;
    private GridView gridView;
    private View dialerButton;
    private Button appsButton;
    private View messageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialerButton = findViewById(R.id.activity_main_dialerButton);
        appsButton = (Button) findViewById(R.id.activity_main_appsButton);
        messageButton = findViewById(R.id.activity_main_messageButton);

        dialerButton.setOnClickListener(this);
        appsButton.setOnClickListener(this);
        messageButton.setOnClickListener(this);

        gridView = (GridView) findViewById(R.id.gridview);
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
        return super.onOptionsItemSelected(item);
    }

    private void setScreenOrientation(){
        Resources resources = getResources();
        if(resources.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            gridView.setNumColumns(resources.getInteger(R.integer.portrait_column_number));
        else if (resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            gridView.setNumColumns(resources.getInteger(R.integer.landscape_column_number));

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.activity_main_appsButton) {
            Intent intent = new Intent(this, AllAppsActivity.class);
            startActivity(intent);
        } else if (id == R.id.activity_main_dialerButton) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            startActivity(intent);
        } else if (id == R.id.activity_main_messageButton) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setType("vnd.android-dir/mms-sms");
            startActivity(intent);
        }
    }
}
