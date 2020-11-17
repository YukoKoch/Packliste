package com.yuko.packliste;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;

public class IOList {
    private ArrayList<PackingItem> packingItems = new ArrayList<>();
    private ArrayList<Category> listOfCategories = new ArrayList<>();
    private ArrayList<Person> listOfPeople = new ArrayList<>();
    private SharedPreferences preferences;

    public ArrayList<PackingItem> getPackingItems() {
        return packingItems;
    }

    public ArrayList<Category> getCategories() {
        return listOfCategories;
    }

    public void addCategory(Category category) {
        boolean alreadyExists = listOfCategories.stream().anyMatch((Category c) ->
                c.getName().equals(category.getName()));
        if (!alreadyExists) {
            listOfCategories.add(category);
        }
    }

    public void addCategory(String name) {
        Category category = new Category(name);
        addCategory(category);
    }

    public ArrayList<Person> getPeople() {
        return listOfPeople;
    }

    public void addPerson(Person person) {
        boolean alreadyExists = listOfPeople.stream().anyMatch((Person p) ->
                p.getName().equals(person.getName()));
        if (!alreadyExists) {
            listOfPeople.add(person);
        }
    }

    public void addPerson(String name) {
        Person person = new Person(name);
        addPerson(person);
    }

    public void addPackingItem(PackingItem item) {
        packingItems.add(item);
        item.getListOfCategories().forEach((Category category) -> {
            addCategory(category);
        });
        item.getListOfPeople().forEach((Person person) -> {
            addPerson(person);
        });
        writePackingItemsToFile();
    }

    public void initialize(SharedPreferences preferences) {
        this.preferences = preferences;
        readPackingItemsFromFile();
    }

    private void writePackingItemsToFile() {
        String output = getStringFromItems();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("packingItems", output);
        editor.apply();
    }

    private void readPackingItemsFromFile() {
        String packingItemsString = preferences.getString("packingItems", "").trim();
        parseItems(packingItemsString);
    }

    private String getStringFromItems() {
        final String[] output = {""};
        packingItems.forEach((PackingItem item) -> {
            output[0] = output[0].concat(getStringFromItem(item));
            output[0] = output[0].concat("\n");
        });
        return output[0];
    }

    private String getStringFromItem(PackingItem item) {
        final String[] output = {""};
        output[0] = output[0].concat(item.getName());
        output[0] = output[0].concat("/c:/");
        item.getListOfCategories().forEach((Category category) -> {
            output[0] = output[0].concat(category.getName());
            output[0] = output[0].concat("_");
        });
        output[0] = output[0].concat("/p:/");
        item.getListOfPeople().forEach((Person person) -> {
            output[0] = output[0].concat(person.getName());
            output[0] = output[0].concat("_");
        });
        output[0] = output[0].concat("/t:/");
        output[0] = output[0].concat(String.valueOf(item.isChecked()));

        return output[0];
    }

    private void parseItems(String itemsString) {
        String[] items = itemsString.split("\n");
        Arrays.asList(items).forEach((String itemString) -> {
            String name = itemString.split(("/c:/"))[0];
            PackingItem item = new PackingItem(name);
            String remainder = itemString.split(("/c:/"))[1];
            String categoriesString = remainder.split("/p:/")[0];
            String[] categoriesList = categoriesString.split("_");
            Arrays.asList(categoriesList).forEach((String category) -> {
                if (category.length() > 0) {
                    item.addCategory(category);
                    addCategory(category);
                }
            });
            remainder = remainder.split("/p:/")[1];
            String peopleString = remainder.split("/t:/")[0];
            String[] peopleList = peopleString.split("_");
            Arrays.asList(peopleList).forEach((String person) -> {
                if (person.length() > 0) {
                    item.addPerson(person);
                    addPerson(person);
                }
            });
            remainder = remainder.split("/t:/")[1];
            item.setChecked(remainder.equals("true"));
            packingItems.add(item);
        });
    }

    public String getCategoryCount(String name) {
        int checked = 0;
        int unchecked = 0;
        for (int i = 0; i < getPackingItems().size(); ++i) {
            PackingItem item = getPackingItems().get(i);
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
