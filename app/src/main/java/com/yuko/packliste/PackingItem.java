package com.yuko.packliste;

import java.util.ArrayList;

public class PackingItem {
    private String name;
    private ArrayList<Category> listOfCategories;
    private boolean isChecked;

    public PackingItem(String name) {
        this.name = name;
        this.listOfCategories = new ArrayList<>();
        this.isChecked = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Category> getListOfCategories() {
        return listOfCategories;
    }

    public void setListOfCategories(ArrayList<Category> listOfCategories) {
        this.listOfCategories = listOfCategories;
    }

    public void addCategory(String name) {
        Category category = new Category(name);
        this.listOfCategories.add(category);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
