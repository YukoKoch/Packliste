package com.yuko.packliste;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;

public class IOList {
    private final ArrayList<PackingItem> packingItems = new ArrayList<>();
    private final ArrayList<Category> listOfCategories = new ArrayList<>();
    private SharedPreferences preferences;

    public ArrayList<PackingItem> getPackingItems() {
        return packingItems;
    }

    public ArrayList<PackingItem> getPackingItems(String category) {
        ArrayList<PackingItem> unpacked_items = new ArrayList<>();
        ArrayList<PackingItem> packed_items = new ArrayList<>();
        packingItems.forEach((PackingItem item) -> {
            String[] searchedStrings = category.split("Suche nach: ");
            if (searchedStrings.length > 1) {
                if (item.getName().toLowerCase().contains(searchedStrings[1].toLowerCase())) {
                    if (item.isChecked()) {
                        packed_items.add(item);
                    } else {
                        unpacked_items.add(item);
                    }
                }
            } else if (category.equals("Alles")) {
                if (item.isChecked()) {
                    packed_items.add(item);
                } else {
                    unpacked_items.add(item);
                }
            } else {
                item.getListOfCategories().forEach((Category categoryObject) -> {
                    if (categoryObject.getName().equals(category)) {
                        if (item.isChecked()) {
                            packed_items.add(item);
                        } else {
                            unpacked_items.add(item);
                        }
                    }
                });
            }
        });
        ArrayList<PackingItem> items = new ArrayList<>();
        items.addAll(unpacked_items);
        items.addAll(packed_items);
        return items;
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

    public void addPackingItem(PackingItem item) {
        boolean alreadyExists = packingItems.stream().anyMatch((PackingItem item1) ->
                itemsMatch(item1, item));
        if (!alreadyExists) {
            item.getListOfCategories().forEach(this::addCategory);
            if (item.getListOfCategories().size() == 0) {
                addCategory("Unsortiert");
                item.addCategory("Unsortiert");
            }
            packingItems.add(item);
            writePackingItemsToFile();
        }
    }

    public void initialize(SharedPreferences preferences) {
        this.preferences = preferences;
        if (packingItems.size() == 0) {
            readPackingItemsFromFile();
        }
    }

    private boolean itemsMatch(PackingItem item1, PackingItem item2) {
        boolean namesMatch = item1.getName().equals(item2.getName());
        return namesMatch && categoriesMatch(item1, item2);
    }

    private boolean categoriesMatch(PackingItem item1, PackingItem item2) {
        ArrayList<Category> categories1 = item1.getListOfCategories();
        ArrayList<Category> categories2 = item2.getListOfCategories();
        if (categories1.size() != categories2.size()) {
            return false;
        }
        for (int i = 0; i < categories1.size(); ++i) {
            if (!categories1.get(i).getName().equals(categories2.get(i).getName())) {
                return false;
            }
        }
        return true;
    }

    public void setCheckedForAllItems(boolean isChecked) {
        packingItems.forEach(item -> item.setChecked(isChecked));
        writePackingItemsToFile();
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
        cleanUpCategories();
    }

    private void cleanUpCategories() {
        listOfCategories.clear();
        packingItems.forEach(packingItem -> packingItem.getListOfCategories().forEach(this::addCategory));
    }

    public void clearData() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("packingItems", "");
        editor.apply();
        packingItems.clear();
        listOfCategories.clear();
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

    public void importItems(String string) {
        parseItems(string);
    }

    public void updateCheckedStatus(PackingItem item) {
        packingItems.forEach(item1 -> {
            if (itemsMatch(item1, item)) {
                item1.setChecked(item.isChecked());
            }
        });
        writePackingItemsToFile();
    }

    public void updateItem(PackingItem oldItem, PackingItem newItem) {
        for (int i = 0; i < packingItems.size(); ++i) {
            PackingItem packingItem = packingItems.get(i);
            if (itemsMatch(packingItem, oldItem)) {
                packingItems.set(i, newItem);
            }
        }
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
        output[0] = output[0].concat("/t:/");
        output[0] = output[0].concat(Boolean.toString(item.isChecked()));

        return output[0];
    }

    private void legacyParseWithPeople(PackingItem item, String remainder) {
        String[] categoriesArray = remainder.split("/p:/");
        String categoriesString = categoriesArray[0];
        String[] categoriesList = categoriesString.split("_");
        Arrays.asList(categoriesList).forEach((String category) -> {
            if (category.length() > 0) {
                item.addCategory(category);
                addCategory(category);
            }
        });
        remainder = categoriesArray[1];
        String[] peopleArray = remainder.split("/t:/");
        String peopleString = peopleArray[0];
        String[] peopleList = peopleString.split("_");
        Arrays.asList(peopleList).forEach((String person) -> {
            if (person.length() > 0) {
                item.addCategory(person);
                addCategory(person);
            }
        });
        if (peopleArray.length < 2) {
            addPackingItem(item);
            return;
        }
        remainder = peopleArray[1];
        item.setChecked(remainder.equals("true"));
        addPackingItem(item);
    }

    private void parseItems(String itemsString) {
        String[] items = itemsString.split("\n");
        Arrays.asList(items).forEach((String itemString) -> {
            if (itemsString.equals("")) {
                return;
            }
            String[] itemArray = itemString.split(("/c:/"));
            String name = itemArray[0];
            PackingItem item = new PackingItem(name);
            if (itemArray.length < 2) {
                addPackingItem(item);
                return;
            }
            String remainder = itemArray[1];
            String[] categoriesArray = remainder.split("/p:/");
            // If /p:/ was found inside the string, then categoriesArray has more than one value.
            // Therefore, the legacy parse should be used, which parses the strings that still
            // contained people. Nowadays, people code is removed.
            if (categoriesArray.length > 1) {
                legacyParseWithPeople(item, remainder);
                return;
            }
            categoriesArray = remainder.split("/t:/");
            String categoriesString = categoriesArray[0];
            String[] categoriesList = categoriesString.split("_");
            Arrays.asList(categoriesList).forEach((String category) -> {
                if (category.length() > 0) {
                    item.addCategory(category);
                    addCategory(category);
                }
            });
            if (categoriesArray.length < 2) {
                addPackingItem(item);
                return;
            }
            remainder = categoriesArray[1];
            item.setChecked(remainder.equals("true"));
            addPackingItem(item);
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
        }
        return "".concat(Integer.toString((unchecked))).concat("/").concat(Integer.toString(checked + unchecked));
    }

    public String getUncheckedCount() {
        int unchecked = 0;
        for (int i = 0; i < getPackingItems().size(); ++i) {
            PackingItem item = getPackingItems().get(i);
            if (!item.isChecked()) {
                unchecked++;
            }
        }
        return "".concat(Integer.toString((unchecked))).concat("/").concat(Integer.toString(getPackingItems().size()));
    }

    public ArrayList<CategoryListItem> getCategoryList() {
        ArrayList<CategoryListItem> items = new ArrayList<>();
        listOfCategories.forEach((Category category) -> {
            CategoryListItem item = new CategoryListItem(category.getName(), getCategoryCount(category.getName()));
            items.add(item);
        });
        return items;
    }

    public ArrayList<SimpleNameListItem> getSimpleNameCategoryList() {
        ArrayList<SimpleNameListItem> items = new ArrayList<>();
        listOfCategories.forEach(category -> items.add(new SimpleNameListItem(category.getName(), false)));
        return items;
    }

    public ArrayList<SimpleNameListItem> getSimpleNameCategoryList(PackingItem item) {
        ArrayList<SimpleNameListItem> items = new ArrayList<>();
        listOfCategories.forEach(category -> {
            boolean isChecked = item.getListOfCategories().stream().anyMatch(category1 -> category.getName().equals(category1.getName()));
            items.add(new SimpleNameListItem(category.getName(), isChecked));
        });
        return items;
    }
}
