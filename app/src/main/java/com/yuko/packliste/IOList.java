package com.yuko.packliste;

import android.content.SharedPreferences;

import java.lang.reflect.Array;
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

    public ArrayList<PackingItem> getPackingItems(String category) {
        ArrayList<PackingItem> unpacked_items = new ArrayList<>();
        ArrayList<PackingItem> packed_items = new ArrayList<>();
        packingItems.forEach((PackingItem item) -> {
            item.getListOfCategories().forEach((Category categoryObject) -> {
                if (categoryObject.getName().equals(category)) {
                    if (item.isChecked()) {
                        packed_items.add(item);
                    } else {
                        unpacked_items.add(item);
                    }
                }
            });
            item.getListOfPeople().forEach((Person person) -> {
                if (person.getName().equals(category)) {
                    if (item.isChecked()) {
                        packed_items.add(item);
                    } else {
                        unpacked_items.add(item);
                    }
                }
            });
        });
        ArrayList<PackingItem> items = new ArrayList<>();
        items.addAll(unpacked_items);
        items.addAll(packed_items);
        return items;
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
        if (packingItems.size() == 0) {
            readPackingItemsFromFile();
        }
    }

    private boolean itemsMatch(PackingItem item1, PackingItem item2) {
        return item1.getName().equals(item2.getName())
                && item1.getListOfCategories().equals(item2.getListOfCategories())
                && item1.getListOfPeople().equals(item2.getListOfPeople());
    }

    public void removeItem(PackingItem item) {
        ArrayList<PackingItem> items = new ArrayList<>();
        packingItems.forEach(packingItem -> {
            if (!itemsMatch(packingItem, item)) {
                items.add(packingItem);
            }
        });
        packingItems.clear();
        packingItems.addAll(items);
        writePackingItemsToFile();
        cleanUpCategoriesAndPeople();
    }

    private void cleanUpCategoriesAndPeople() {
        listOfCategories.clear();
        listOfPeople.clear();
        packingItems.forEach(packingItem -> {
            packingItem.getListOfCategories().forEach(category -> {
                addCategory(category);
            });
            packingItem.getListOfPeople().forEach(person -> {
                addPerson(person);
            });
        });
    }

    public void clearData() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("packingItems", "");
        editor.apply();
        packingItems.clear();
        listOfCategories.clear();
        listOfPeople.clear();
    }

    private void writePackingItemsToFile() {
        String output = getStringFromItems();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("packingItems", output);
        editor.apply();
    }

    public String getExportText() {
        return getStringFromItems();
    }

    private void readPackingItemsFromFile() {
        String packingItemsString = preferences.getString("packingItems", "").trim();
        parseItems(packingItemsString);
    }

    public void updateCheckedStatus(PackingItem item) {
        for (int i = 0; i < packingItems.size(); ++i) {
            PackingItem packingItem = packingItems.get(i);
            if (itemsMatch(packingItem, item)) {
                packingItems.set(i, item);
            }
        };
        writePackingItemsToFile();
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
            if (itemsString.equals("")) {
                return;
            }
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

    public ArrayList<CategoryListItem> getCategoryList() {
        ArrayList<CategoryListItem> items = new ArrayList<>();
        listOfCategories.forEach((Category category) -> {
            CategoryListItem item = new CategoryListItem(category.getName(), getCategoryCount(category.getName()));
            items.add(item);
        });
        listOfPeople.forEach((Person person) -> {
            CategoryListItem item = new CategoryListItem(person.getName(), getCategoryCount(person.getName()));
            items.add(item);
        });
        return items;
    }

    public ArrayList<SimpleNameListItem> getSimpleNameCategoryList() {
        ArrayList<SimpleNameListItem> items = new ArrayList<>();
        listOfCategories.forEach(category -> items.add(new SimpleNameListItem(category.getName(), false)));
        return items;
    }

    public ArrayList<SimpleNameListItem> getSimpleNamePeopleList() {
        ArrayList<SimpleNameListItem> items = new ArrayList<>();
        listOfPeople.forEach(person -> items.add(new SimpleNameListItem(person.getName(), false)));
        return items;
    }
}
