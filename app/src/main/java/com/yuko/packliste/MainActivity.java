package com.yuko.packliste;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddItemDialogFragment.OnItemAddedListener, ImportDialogFragment.OnImportListener, SearchDialogFragment.OnSearchListener {
    public static final String DETAIL_VIEW_CATEGORY = "com.yuko.packliste.MESSSAGE";

    private static final IOList ioList = new IOList();
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
        categoryList.add(0, new CategoryListItem("Alles", ioList.getUncheckedCount()));
        adapter = new OverviewListItemAdapter(this, categoryList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DETAIL_VIEW_CATEGORY, categoryList.get(position).getName());
            startActivity(intent);
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            AddItemDialogFragment dialogFragment = new AddItemDialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "AddItemDialogFragment");
        });
    }

    private void refresh() {
        adapter.clear();
        ArrayList<CategoryListItem> categoryList = ioList.getCategoryList();
        categoryList.add(0, new CategoryListItem("Alles", ioList.getUncheckedCount()));
        adapter.addAll(categoryList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
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

        if (id == R.id.action_import) {
            ImportDialogFragment importDialogFragment = new ImportDialogFragment();
            importDialogFragment.show(getSupportFragmentManager(), "ImportDialogFragment");
            return true;
        } else if (id == R.id.action_export) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, ioList.getExportText());
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
            return true;
        } else if (id == R.id.action_check) {
            ioList.setCheckedForAllItems(true);
            refresh();
        } else if (id == R.id.action_uncheck) {
            ioList.setCheckedForAllItems(false);
            refresh();
        } else if (id == R.id.action_clear_data) {
            ioList.clearData();
            refresh();
            return true;
        } else if (id == R.id.action_search) {
            SearchDialogFragment searchDialogFragment = new SearchDialogFragment();
            searchDialogFragment.show(getSupportFragmentManager(), "SearchDialogFragment");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemAdded(String name, ArrayList<String> categories) {
        PackingItem packingItem = new PackingItem(name);
        categories.forEach(packingItem::addCategory);
        ioList.addPackingItem(packingItem);
        refresh();
    }

    @Override
    public ArrayList<SimpleNameListItem> getCategoryList() {
        return ioList.getSimpleNameCategoryList();
    }

    public static IOList getIOList() {
        return ioList;
    }

    @Override
    public void addCategory(String name) {
        ioList.addCategory(name);
        refresh();
    }

    @Override
    public void onImport(String string) {
        ioList.importItems(string);
        refresh();
    }

    @Override
    public void onSearch(String string) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DETAIL_VIEW_CATEGORY, "Suche nach: " + string);
        startActivity(intent);
    }
}