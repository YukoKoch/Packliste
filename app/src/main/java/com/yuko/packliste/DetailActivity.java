package com.yuko.packliste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String category = intent.getStringExtra(MainActivity.DETAIL_VIEW_CATEGORY);
        showItemList(category);
    }

    private void showItemList(String category) {
        TextView view = (TextView) findViewById(R.id.categoryDetailName);
        view.setText(category);
    }
}