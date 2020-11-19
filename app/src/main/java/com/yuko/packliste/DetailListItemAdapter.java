package com.yuko.packliste;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DetailListItemAdapter extends ArrayAdapter<PackingItem> {
    private Context context;
    private ArrayList<PackingItem> items = new ArrayList<>();

    public DetailListItemAdapter(Context context, ArrayList<PackingItem> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
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
        }

        return listItem;
    }
}

