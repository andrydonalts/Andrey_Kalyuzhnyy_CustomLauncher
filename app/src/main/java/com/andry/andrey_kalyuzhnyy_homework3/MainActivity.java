package com.andry.andrey_kalyuzhnyy_homework3;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private View dialerButton;
    private Button appsButton;
    private View messageButton;
    private GridLayout gridLayout;
    private AppsManager appsManager;
    private ArrayList<AppsDetail> mainScreenApps;


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

        appsManager = new AppsManager(this);
        appsManager.loadMainScreenApps();
        mainScreenApps = new ArrayList<>(15);
        mainScreenApps.addAll(appsManager.getMainScreenApps());

        setGridLayout();

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

    class LongPressListener implements View.OnLongClickListener{

        @Override
        public boolean onLongClick(View view) {
            final ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            view.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    // putting views in gridlayout dynamically
    public void setGridLayout() {
        gridLayout = (GridLayout) findViewById(R.id.activity_main_gridLayout);
        gridLayout.setOnDragListener(new DragListener());

        LayoutInflater layoutInflater = (LayoutInflater)
                this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (isPortraitOrientation() == false) {
            gridLayout.setColumnCount(5);
            gridLayout.setRowCount(3);
        }

        for (int i = 0; i < 15; i++) {

            final AppsDetail appsDetail = mainScreenApps.get(i);
            View appItemView = layoutInflater.inflate(R.layout.app_item, null, false);


            ImageView icon = (ImageView) appItemView.findViewById(R.id.app_item_icon);
            TextView label = (TextView) appItemView.findViewById(R.id.app_item_label);
            icon.setImageDrawable(appsDetail.getIcon());
            label.setText(appsDetail.getLabel());

            appItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appsManager.startApp(appsDetail);
                }
            });
            appItemView.setOnLongClickListener(new LongPressListener());

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = 0;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(3, 3, 3, 3);
            appItemView.setLayoutParams(params);
            gridLayout.addView(appItemView);

        }
    }

    class DragListener implements View.OnDragListener{

        @Override
        public boolean onDrag(View v, DragEvent event) {
            View view = (View) event.getLocalState();
            switch (event.getAction()){
                case DragEvent.ACTION_DRAG_LOCATION :
                    if (view == v) {
                        return true;
                    }

                    int index = calculateNewIndex(event.getX(), event.getY());

                    gridLayout.removeView(view);
                    gridLayout.addView(view, index);
                    break;
                case DragEvent.ACTION_DROP :
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED :
                    if (!event.getResult())
                        view.setVisibility(View.VISIBLE);
            }
            return true;
        }
    }


    private int calculateNewIndex(float x, float y) {

        float cellWidth = gridLayout.getWidth() / gridLayout.getColumnCount();
        int column = (int) (x /cellWidth);

        float cellHeight = gridLayout.getHeight() / gridLayout.getRowCount();
        int row = (int) Math.floor(y / cellHeight);

        int index = row * gridLayout.getColumnCount() + column;
        if (index >= gridLayout.getChildCount())
            index = gridLayout.getChildCount() - 1;

        Log.d("calcNewInd", "row = " + Integer.toString(row) + " col = " + Integer.toString(column) + " index = " + Integer.toString(index));
        return index;
    }

    private boolean isPortraitOrientation(){
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

}
