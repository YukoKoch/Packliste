package com.yuko.packliste;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class IOList {
    private ArrayList<PackingItem> packingItems = new ArrayList<>();
    private ArrayList<Category> listOfCategories = new ArrayList<>();
    private ArrayList<Person> listOfPeople = new ArrayList<>();
    private boolean isInitialized;

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

    public void addPackingItem(PackingItem item) {
        packingItems.add(item);
        item.getListOfCategories().forEach((Category category) -> {
            addCategory(category);
        });
        item.getListOfPeople().forEach((Person person) -> {
            addPerson(person);
        });
    }

    public void initialize() {
        if (isInitialized) {
            return;
        }

        PackingItem fliegenklatsche = new PackingItem("Fliegenklatsche");
        Category wohnmobil = new Category("Wohnmobil");
        fliegenklatsche.addCategory(wohnmobil);
        addCategory(wohnmobil);
        PackingItem bettlakenAnna = new PackingItem("Bettlaken");
        Person anna = new Person("Anna");
        bettlakenAnna.addPerson(anna);
        addPerson(anna);
        packingItems.add(fliegenklatsche);
        packingItems.add(bettlakenAnna);

        writePackingItemsToFile();
        isInitialized = true;
    }

    private void writePackingItemsToFile() {
        File external = Environment.getExternalStorageDirectory();
        File packingListFile = new File(external, "packingList.txt");
        String output = getStringFromItems();
        try {
            FileOutputStream fos = new FileOutputStream(packingListFile);
            fos.write(output.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getStringFromItems() {
        String output = "";
        packingItems.forEach((PackingItem item) -> {
            output.concat(getStringFromItem(item));
            output.concat("\n");
        });
        return output;
    }

    private String getStringFromItem(PackingItem item) {
        String output = "";
        output.concat(item.getName());
        output.concat("/c:/");
        item.getListOfCategories().forEach((Category category) -> {
            output.concat(category.getName());
        });
        output.concat("/p:/");
        item.getListOfPeople().forEach((Person person) -> {
            output.concat(person.getName());
        });
        output.concat("/e/");

        return output;
    }
}
