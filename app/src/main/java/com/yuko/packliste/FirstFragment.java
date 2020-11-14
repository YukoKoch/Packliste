package com.yuko.packliste;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;

public class FirstFragment extends Fragment {

    private IOList ioList = new IOList();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        ioList.initialize();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        refreshList();

        view.findViewById(R.id.categoryList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    public void refreshList() {
        LinearLayout overviewListItem = (LinearLayout) getView().findViewById(R.id.categoryList);
        if (overviewListItem != null) {
            overviewListItem.removeAllViews();
            ioList.getCategories().forEach((Category category) -> {
                View item = getLayoutInflater().inflate(R.layout.overview_list_item, null);
                ((TextView) item.findViewById(R.id.categoryName)).setText(category.getName());
                ((TextView) item.findViewById(R.id.categoryNumber)).setText(getItemCount(category.getName()));
                overviewListItem.addView(item);
            });
            ioList.getPeople().forEach((Person person) -> {
                View item = getLayoutInflater().inflate(R.layout.overview_list_item, null);
                ((TextView) item.findViewById(R.id.categoryName)).setText(person.getName());
                ((TextView) item.findViewById(R.id.categoryNumber)).setText(getItemCount(person.getName()));
                overviewListItem.addView(item);
            });
        }
    }

    private String getItemCount(String name) {
        int checked = 0;
        int unchecked = 0;
        for (int i = 0; i < ioList.getPackingItems().size(); ++i) {
            PackingItem item = ioList.getPackingItems().get(i);
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
}