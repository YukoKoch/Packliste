package com.yuko.packliste;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DetailListItemAdapter extends ArrayAdapter<PackingItem> {
    private final Context context;
    private final ArrayList<PackingItem> items;
    private final OnItemCheckedListener listener;

    public DetailListItemAdapter(Context context, ArrayList<PackingItem> items, OnItemCheckedListener listener) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    public interface OnItemCheckedListener {
        void onItemChecked(PackingItem item);
        void onItemLongPressed(PackingItem item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.detail_list_item, parent, false);
        }

        if (items.size() > position) {
            PackingItem item = items.get(position);
            CheckBox checkBox = (CheckBox) listItem.findViewById(R.id.checkBox);
            checkBox.setText(item.getName());
            checkBox.setChecked(item.isChecked());
            checkBox.setOnClickListener(v -> {
                CheckBox checkBox1 = (CheckBox) v;
                String name = checkBox1.getText().toString();
                boolean isChecked = checkBox1.isChecked();
                items.forEach(packingItem -> {
                    if (packingItem.getName().equals(name)) {
                        packingItem.setChecked(isChecked);
                        listener.onItemChecked(packingItem);
                    }
                });
            });
            checkBox.setOnLongClickListener(v -> {
                CheckBox checkBox1 = (CheckBox) v;
                String name = checkBox1.getText().toString();
                PackingItem itemInQuestion = new PackingItem("");
                items.forEach(packingItem -> {
                    if (packingItem.getName().equals(name)) {
                        // remove checkBox first on crash?
                        itemInQuestion.setName(packingItem.getName());
                        itemInQuestion.setListOfCategories(packingItem.getListOfCategories());
                        itemInQuestion.setChecked(packingItem.isChecked());
                    }
                });
                listener.onItemLongPressed(itemInQuestion);
                return true;
            });
        }
        return listItem;
    }
}

