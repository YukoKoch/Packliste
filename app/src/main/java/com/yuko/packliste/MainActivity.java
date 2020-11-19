package com.yuko.packliste;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    public static final String DETAIL_VIEW_CATEGORY = "com.yuko.packliste.MESSSAGE";

    private static IOList ioList = new IOList();
    private OverviewListItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ioList.initialize(getPreferences(Context.MODE_PRIVATE));

        ListView listView = (ListView) findViewById(R.id.categoryList);
        ArrayList<CategoryListItem> categoryList = ioList.getCategoryList();
        adapter = new OverviewListItemAdapter(this, categoryList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DETAIL_VIEW_CATEGORY, ioList.getCategoryList().get(position).getName());
            startActivity(intent);
        });

        AtomicInteger counter = new AtomicInteger();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            PackingItem packingItem = new PackingItem("MainActivity");
            packingItem.addCategory("App".concat(String.valueOf(counter.getAndIncrement())));
            ioList.addPackingItem(packingItem);
            adapter.clear();
            adapter.addAll(ioList.getCategoryList());
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_clear_data) {
            ioList.clearData();
            adapter.clear();
            adapter.addAll(ioList.getCategoryList());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static IOList getIOList() {
        return ioList;
    }
}