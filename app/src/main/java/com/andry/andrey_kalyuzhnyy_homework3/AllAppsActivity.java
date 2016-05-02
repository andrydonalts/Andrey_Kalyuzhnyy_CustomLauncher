package com.andry.andrey_kalyuzhnyy_homework3;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class AllAppsActivity extends AppCompatActivity implements AppDeleteFragmentDialog.DeleteAppInterface {

    private static final int REQUEST_UNINSTALL = 1;
    private Adapter adapter;
    private GridView gridView;
    private EditText searchEditText;
    private AppsManager appsManager;
    private ArrayList<AppsDetail> apps;
    boolean inGridMode = true;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_apps);

        searchEditText = (EditText) findViewById(R.id.activity_all_apps_edit);

        gridView = (GridView) findViewById(R.id.activity_all_apps_gridview);
        adapter = new Adapter(this);
        gridView.setAdapter(adapter);

        searchEditText.addTextChangedListener(textWatcher);
        setScreenOrientation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_all_apps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.menu_all_apps_layout_icon) {
           changeMode();
        }

        return super.onOptionsItemSelected(item);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable query) {
            adapter.getFilter().filter(query);
        }
    };

    private void changeMode() {
        if (menu != null) {
            MenuItem item = menu.findItem(R.id.menu_all_apps_layout_icon);
            if (item != null && inGridMode == true) {
                item.setIcon(R.drawable.ic_grid_on_white_24dp);
                gridView.setNumColumns(1);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "In Linear Mode", Toast.LENGTH_SHORT).show();

                inGridMode = false;
            } else if (item != null && inGridMode == false) {
                item.setIcon(R.drawable.ic_view_headline_white_24dp);
                Toast.makeText(this, "In Grid Mode", Toast.LENGTH_SHORT).show();
                inGridMode = true;
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    gridView.setNumColumns(3);
                    adapter.notifyDataSetChanged();
                } else {
                    gridView.setNumColumns(5);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void setScreenOrientation() {
        Resources resources = getResources();
        if (resources.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            gridView.setNumColumns(resources.getInteger(R.integer.portrait_column_number));
        else if (resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            gridView.setNumColumns(resources.getInteger(R.integer.landscape_column_number));
    }

    @Override
    public void deleteApp(String packageName) {
        Log.d("AllAppsActivity", packageName + " app will be deleted");

        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
        intent.setData(Uri.parse("package:" + packageName));

        startActivityForResult(intent, REQUEST_UNINSTALL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_UNINSTALL) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Uninstall succeeded!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Uninstall canceled!", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Uninstall Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
