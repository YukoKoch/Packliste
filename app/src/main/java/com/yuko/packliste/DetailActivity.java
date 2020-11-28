package com.yuko.packliste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements DetailListItemAdapter.OnItemCheckedListener, RemoveOrEditItemDialogFragment.OnItemRemovedListener, AddItemDialogFragment.OnItemEditedListener {

    private IOList ioList = new IOList();
    private DetailListItemAdapter adapter;
    private String category = "";

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
        this.category = category;

        TextView view = (TextView) findViewById(R.id.categoryDetailName);
        view.setText(category);

        ListView listView = (ListView) findViewById(R.id.itemList);
        ArrayList<PackingItem> items = ioList.getPackingItems(category);
        adapter = new DetailListItemAdapter(this, items, this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemChecked(PackingItem item) {
        ioList.updateCheckedStatus(item);
    }

    @Override
    public void onItemLongPressed(PackingItem item) {
        RemoveOrEditItemDialogFragment dialogFragment = new RemoveOrEditItemDialogFragment(item);
        dialogFragment.show(getSupportFragmentManager(), "RemoveOrEditItemDialogFragment");
    }

    @Override
    public void onItemRemovedOK(PackingItem item) {
        ioList.removeItem(item);
        adapter.clear();
        adapter.addAll(ioList.getPackingItems(category));
    }

    @Override
    public void onEditItem(PackingItem item) {
        AddItemDialogFragment dialogFragment = new AddItemDialogFragment(item);
        dialogFragment.show(getSupportFragmentManager(), "AddItemDialogFragment");
    }

    @Override
    public void onItemEdited(PackingItem oldItem, PackingItem newItem) {
        ioList.updateItem(oldItem, newItem);
        adapter.clear();
        adapter.addAll(ioList.getPackingItems(category));
    }

    @Override
    public ArrayList<SimpleNameListItem> getCategoryList(PackingItem item) {
        return ioList.getSimpleNameCategoryList(item);
    }

    @Override
    public ArrayList<SimpleNameListItem> getPeopleList(PackingItem item) {
        return ioList.getSimpleNamePeopleList(item);
    }

    @Override
    public void addCategory(String name) {
        ioList.addCategory(name);
    }

    @Override
    public void addPerson(String name) {
        ioList.addPerson(name);
    }
}