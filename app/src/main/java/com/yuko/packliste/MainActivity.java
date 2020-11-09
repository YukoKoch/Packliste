package com.yuko.packliste;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Category> listOfCategories = new ArrayList<>();
    private ArrayList<Person> listOfPeople = new ArrayList<>();
    private ArrayList<PackingItem> listOfPackingItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        File file = new File(getFilesDir(), "packliste");

        refreshList();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            PackingItem packingItem = new PackingItem("Fliegenklatsche");
            Category category = new Category("Wohnmobil");
            packingItem.addCategory(category);
            listOfPackingItems.add(packingItem);
            listOfCategories.add(category);
            refreshList();
        });
    }

    private void refreshList() {
        LinearLayout overviewListItem = (LinearLayout) findViewById(R.id.categoryList);
        if (overviewListItem != null) {
            overviewListItem.removeAllViews();
        }
        listOfCategories.forEach((Category category) -> {
            View item = getLayoutInflater().inflate(R.layout.overview_list_item, null);
            ((TextView) item.findViewById(R.id.categoryName)).setText(category.getName());
            ((TextView) item.findViewById(R.id.categoryNumber)).setText(getItemCount(category.getName()));
            overviewListItem.addView(item);
        });
        listOfPeople.forEach((Person person) -> {
            View item = getLayoutInflater().inflate(R.layout.overview_list_item, null);
            ((TextView) item.findViewById(R.id.categoryName)).setText(person.getName());
            ((TextView) item.findViewById(R.id.categoryNumber)).setText(getItemCount(person.getName()));
            overviewListItem.addView(item);
        });
    }

    private String getItemCount(String name) {
        int checked = 0;
        int unchecked = 0;
        for (int i = 0; i < listOfPackingItems.size(); ++i) {
            PackingItem item = listOfPackingItems.get(i);
            ArrayList<Category> itemCategories = item.getListOfCategories();
            for (int j = 0; j < itemCategories.size(); ++j) {
                if (itemCategories.get(j).getName().equals(name)) {
                    if (item.isChecked()) {
                        checked++;
                    } else {
                        unchecked++;
                    }
                }
            }
            ArrayList<Person> itemPeople = item.getListOfPeople();
            for (int j = 0; j < itemPeople.size(); ++j) {
                if (itemPeople.get(j).getName().equals(name)) {
                    if (item.isChecked()) {
                        checked++;
                    } else {
                        unchecked++;
                    }
                }
            }
        }
        return "".concat(Integer.toString((unchecked))).concat("/").concat(Integer.toString(checked + unchecked));
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