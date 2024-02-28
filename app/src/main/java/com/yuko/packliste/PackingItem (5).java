package com.yuko.packliste;

import android.graphics.Matrix;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PackingItem {
    private String name;
    private ArrayList<Person> listOfPeople;
    private ArrayList<Category> listOfCategories;
    private boolean isChecked;

    public PackingItem(String name) {
        this.name = name;
        this.listOfPeople = new ArrayList<Person>();
        this.listOfCategories = new ArrayList<Category>();
        this.isChecked = false;
    }

    public PackingItem(String name, ArrayList<Person> listOfPeople, ArrayList<Category> listOfCategories, boolean isChecked) {
        this.name = name;
        this.listOfPeople = listOfPeople;
        this.listOfCategories = listOfCategories;
        this.isChecked = isChecked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Person> getListOfPeople() {
        return listOfPeople;
    }

    public void setListOfPeople(ArrayList<Person> listOfPeople) {
        this.listOfPeople = listOfPeople;
    }

    public void addPerson(Person person) {
        this.listOfPeople.add(person);
    }

    public void addPerson(String name) {
        Person person = new Person(name);
        this.listOfPeople.add(person);
    }

    public ArrayList<Category> getListOfCategories() {
        return listOfCategories;
    }

    public void setListOfCategories(ArrayList<Category> listOfCategories) {
        this.listOfCategories = listOfCategories;
    }

    public void addCategory(Category category) {
        this.listOfCategories.add(category);
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
