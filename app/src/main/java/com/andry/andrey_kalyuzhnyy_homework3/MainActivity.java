package com.andry.andrey_kalyuzhnyy_homework3;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static String mainScreenApps_Extra = "mainScreenApps_Extra";

    private GridLayout gridLayout;
    private AppsManager appsManager;
    private ArrayList<AppsDetail> mainScreenApps;
    private Toolbar toolbar;
    private TextView toolbarRemoveText;
    private int index;
    private AppsDetail draggedApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View dialerButton;
        Button appsButton;
        View messageButton;

        dialerButton = findViewById(R.id.activity_main_dialerButton);
        appsButton = (Button) findViewById(R.id.activity_main_appsButton);
        messageButton = findViewById(R.id.activity_main_messageButton);
        gridLayout = (GridLayout) findViewById(R.id.activity_main_gridLayout);

        dialerButton.setOnClickListener(new OnClickListener());
        appsButton.setOnClickListener(new OnClickListener());
        messageButton.setOnClickListener(new OnClickListener());

        appsManager = new AppsManager(this);
        appsManager.loadMainScreenApps();
        mainScreenApps = new ArrayList<>(15);
        mainScreenApps.addAll(appsManager.getMainScreenApps());

        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        toolbarRemoveText = (TextView) findViewById(R.id.activity_main_removeText);
        toolbar.setOnDragListener(new DragListener());

        gridLayout.setOnDragListener(new DragListener());
        setGridLayout(gridLayout);
    }

    private class OnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int id = view.getId();

            if (id == R.id.activity_main_appsButton) {
                Intent intent = new Intent(MainActivity.this, AllAppsActivity.class);
                intent.putParcelableArrayListExtra(mainScreenApps_Extra , mainScreenApps);
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

    class LongPressListener implements View.OnLongClickListener{
        @Override
        public boolean onLongClick(View view) {
            final ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            view.setVisibility(View.INVISIBLE);
            index = calculateNewIndex(view.getX(), view.getY());
            draggedApp = mainScreenApps.get(index);
            Log.d("OnDragStart", mainScreenApps.get(index).getLabel());
            return true;
        }
    }


    class DragListener implements View.OnDragListener{

        boolean deleteAppFromList = false;

        @Override
        public boolean onDrag(View v, DragEvent event) {
            toolbarRemoveText.setVisibility(View.VISIBLE);
            View view = (View) event.getLocalState();
            switch (event.getAction()){

                case DragEvent.ACTION_DRAG_LOCATION :
                    if (view == v) {
                        return true;
                    }
                    if (v == toolbar){
                        toolbarRemoveText.setTextColor(Color.RED);
                        return true;
                    }
                    index = calculateNewIndex(event.getX(), event.getY());
                    gridLayout.removeView(view);
                    Log.d("Action_Drag_Location", "index " + Integer.toString(index));
                    gridLayout.addView(view, index);
                    break;

                case DragEvent.ACTION_DROP :
                    actionDrop(v, view);
                    break;

                case DragEvent.ACTION_DRAG_ENDED :
                    actionDragEnded(event, view);
            }
            return true;
        }

        private void actionDrop(View dropView, View view){
            if (dropView == toolbar){
                Log.d("actionDrop", "toolbar drop");
                deleteAppFromList = true;
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
                Log.d("actionDrop", "have to stay visible");
            }
        }

        private void actionDragEnded(DragEvent event, View view){
            toolbarRemoveText.setTextColor(Color.WHITE);
            toolbarRemoveText.setVisibility(View.INVISIBLE);
            if (!event.getResult()) {
                Log.d("actionDragEnded", "have to stay visible");
                view.setVisibility(View.VISIBLE);
            } else {
                Log.d("actionDragEnded", "not visible");

                if (deleteAppFromList == true) {
                    Log.d("actionDragEnded", "deleteAppFromList == true");

                    draggedApp.setOnMainScreen(false);
                    deleteAppFromList = false;
                }
                mainScreenApps.add(index, draggedApp);
            }
        }
    }



    /*
    calculates new index of view in gridlayout when it's dragged
     */
    private int calculateNewIndex(float x, float y) {

        float cellWidth = gridLayout.getWidth() / gridLayout.getColumnCount();
        int column = (int) (x /cellWidth);

        float cellHeight = gridLayout.getHeight() / gridLayout.getRowCount();
        int row = (int) Math.floor(y / cellHeight);

        int index = row * gridLayout.getColumnCount() + column;
        if (index >= gridLayout.getChildCount())
            index = gridLayout.getChildCount() - 1;

        //Log.d("calcNewInd", "row = " + Integer.toString(row) + " col = " + Integer.toString(column) + " index = " + Integer.toString(index));
        return index;
    }

    // putting views in gridlayout dynamically
    public void setGridLayout(GridLayout gridLayout) {

        LayoutInflater layoutInflater = (LayoutInflater)
                this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (!Util.isPortraitOrientation(this)) {
            gridLayout.setColumnCount(5);
            gridLayout.setRowCount(3);
        }

        inflatingGridLayout(layoutInflater);
    }

    private void inflatingGridLayout(LayoutInflater layoutInflater){
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
}
