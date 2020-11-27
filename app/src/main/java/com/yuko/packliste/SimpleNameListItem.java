package com.yuko.packliste;

public class SimpleNameListItem {
    private String name;
    private boolean isChecked;

    public SimpleNameListItem(String name, boolean isChecked) {
        this.name = name;
        this.isChecked = isChecked;
    }

    public String getName() {
        return name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}