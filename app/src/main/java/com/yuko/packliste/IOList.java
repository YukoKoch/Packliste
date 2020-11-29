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
        boolean alreadyExists = packingItems.stream().anyMatch((PackingItem item1) ->
                itemsMatch(item1, item));
        if (!alreadyExists) {
            item.getListOfCategories().forEach((Category category) -> {
                addCategory(category);
            });
            item.getListOfPeople().forEach((Person person) -> {
                addPerson(person);
            });
            if (item.getListOfCategories().size() == 0 && item.getListOfPeople().size() == 0) {
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
        return namesMatch
                && categoriesMatch(item1, item2)
                && peopleMatch(item1, item2);
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

    private boolean peopleMatch(PackingItem item1, PackingItem item2) {
        ArrayList<Person> person1 = item1.getListOfPeople();
        ArrayList<Person> person2 = item2.getListOfPeople();
        if (person1.size() != person2.size()) {
            return false;
        }
        for (int i = 0; i < person1.size(); ++i) {
            if (!person1.get(i).getName().equals(person2.get(i).getName())) {
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
        output[0] = output[0].concat(Boolean.toString(item.isChecked()));

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
            if (item.getListOfPeople().size() > 0) {
                if (!item.getName().endsWith(")")) {
                    final String[] itemName = {item.getName()};
                    itemName[0] = itemName[0].concat(" (");
                    item.getListOfPeople().forEach(person -> {
                        itemName[0] = itemName[0].concat(person.getName());
                        itemName[0] = itemName[0].concat(", ");
                    });
                    itemName[0] = itemName[0].substring(0, itemName[0].length() - 2);
                    itemName[0] = itemName[0].concat(")");
                    item.setName(itemName[0]);
                }
            }
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

    public ArrayList<SimpleNameListItem> getSimpleNameCategoryList(PackingItem item) {
        ArrayList<SimpleNameListItem> items = new ArrayList<>();
        listOfCategories.forEach(category -> {
            boolean isChecked = item.getListOfCategories().stream().anyMatch(category1 -> category.getName().equals(category1.getName()));
            items.add(new SimpleNameListItem(category.getName(), isChecked));
        });
        return items;
    }

    public ArrayList<SimpleNameListItem> getSimpleNamePeopleList() {
        ArrayList<SimpleNameListItem> items = new ArrayList<>();
        listOfPeople.forEach(person -> items.add(new SimpleNameListItem(person.getName(), false)));
        return items;
    }

    public ArrayList<SimpleNameListItem> getSimpleNamePeopleList(PackingItem item) {
        ArrayList<SimpleNameListItem> items = new ArrayList<>();
        listOfPeople.forEach(person -> {
            boolean isChecked = item.getListOfPeople().stream().anyMatch(person1 -> person.getName().equals(person1.getName()));
            items.add(new SimpleNameListItem(person.getName(), isChecked));
        });;
        return items;
    }
}
