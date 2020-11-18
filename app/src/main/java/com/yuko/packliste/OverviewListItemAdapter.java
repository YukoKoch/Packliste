package com.yuko.packliste;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class OverviewListItemAdapter extends ArrayAdapter<CategoryListItem> {
    private Context context;
    private ArrayList<CategoryListItem> items = new ArrayList<>();

    public OverviewListItemAdapter(Context context, ArrayList<CategoryListItem> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.overview_list_item, parent, false);
        }

        if (items.size() > position) {
            CategoryListItem item = items.get(position);
            TextView categoryName = (TextView) listItem.findViewById(R.id.categoryName);
            categoryName.setText(item.getName());

            TextView categoryNumber = (TextView) listItem.findViewById(R.id.categoryNumber);
            categoryNumber.setText(item.getCheckedCount());
        }

        return listItem;
    }
}
