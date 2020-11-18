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
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    public static final String DETAIL_VIEW_CATEGORY = "com.yuko.packliste.MESSSAGE";

    private IOList ioList = new IOList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ioList.initialize(getPreferences(Context.MODE_PRIVATE));
        refreshList();

        AtomicInteger counter = new AtomicInteger();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            PackingItem packingItem = new PackingItem("MainActivity");
            packingItem.addCategory("App".concat(String.valueOf(counter.getAndIncrement())));
            ioList.addPackingItem(packingItem);
            refreshList();
        });
    }

    private void refreshList() {
        LinearLayout overviewListItem = (LinearLayout) findViewById(R.id.categoryList);
        if (overviewListItem != null) {
            overviewListItem.removeAllViews();
            ioList.getCategories().forEach((Category category) -> {
                View item = getLayoutInflater().inflate(R.layout.overview_list_item, null);
                ((TextView) item.findViewById(R.id.categoryName)).setText(category.getName());
                ((TextView) item.findViewById(R.id.categoryNumber)).setText(ioList.getCategoryCount(category.getName()));
                overviewListItem.addView(item);
            });
            ioList.getPeople().forEach((Person person) -> {
                View item = getLayoutInflater().inflate(R.layout.overview_list_item, null);
                ((TextView) item.findViewById(R.id.categoryName)).setText(person.getName());
                ((TextView) item.findViewById(R.id.categoryNumber)).setText(ioList.getCategoryCount(person.getName()));
                overviewListItem.addView(item);
            });
        }
    }

    public void openDetailView(View view) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DETAIL_VIEW_CATEGORY, "App");
        startActivity(intent);
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
        }

        return super.onOptionsItemSelected(item);
    }
}