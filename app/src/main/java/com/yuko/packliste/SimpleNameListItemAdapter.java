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

public class SimpleNameListItemAdapter extends ArrayAdapter<SimpleNameListItem> {
    private final Context context;
    private final ArrayList<SimpleNameListItem> items;
    private final OnDialogItemCheckedListener listener;

    public SimpleNameListItemAdapter(Context context, ArrayList<SimpleNameListItem> items, OnDialogItemCheckedListener listener) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    public interface OnDialogItemCheckedListener {
        void onItemChecked(SimpleNameListItem item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.dialog_list_item, parent, false);
        }

        if (items.size() > position) {
            SimpleNameListItem item = items.get(position);
            CheckBox checkBox = (CheckBox) listItem.findViewById(R.id.dialogListItemCheckbox);
            checkBox.setText(item.getName());
            checkBox.setChecked(item.isChecked());
            checkBox.setOnClickListener(v -> {
                CheckBox checkBox1 = (CheckBox) v;
                String name = checkBox1.getText().toString();
                boolean isChecked = checkBox1.isChecked();
                items.forEach(simpleNameListItem -> {
                    if (simpleNameListItem.getName().equals(name)) {
                        simpleNameListItem.setChecked(isChecked);
                        listener.onItemChecked(simpleNameListItem);
                    }
                });
            });
        }

        return listItem;
    }
}
