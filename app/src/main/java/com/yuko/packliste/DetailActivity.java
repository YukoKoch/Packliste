package com.yuko.packliste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private IOList ioList = new IOList();
    private DetailListItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ioList = MainActivity.getIOList();

        Intent intent = getIntent();
        String category = intent.getStringExtra(MainActivity.DETAIL_VIEW_CATEGORY);
        showItemList(category);
    }

    private void showItemList(String category) {
        TextView view = (TextView) findViewById(R.id.categoryDetailName);
        view.setText(category);

        ListView listView = (ListView) findViewById(R.id.itemList);
        ArrayList<PackingItem> items = ioList.getPackingItems(category);
        adapter = new DetailListItemAdapter(this, items);
        listView.setAdapter(adapter);
    }
}