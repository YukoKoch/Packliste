package com.yuko.packliste;

public class CategoryListItem {

    private String name;
    private String checkedCount;

    public CategoryListItem(String name, String checkedCount) {
        this.name = name;
        this.checkedCount = checkedCount;
    }

    public String getName() {
        return name;
    }

    public String getCheckedCount() {
        return checkedCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCheckedCount(String checkedCount) {
        this.checkedCount = checkedCount;
    }
}
