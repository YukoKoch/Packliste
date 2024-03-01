package com.yuko.packliste;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CategoryListItemTest {
    private final String NAME = "bedroom";
    private final String COUNT = "01/03";
    @Test
    public void testGetName() {
        CategoryListItem item = new CategoryListItem(NAME, COUNT);
        assertEquals(item.getName(), NAME);
    }

    @Test
    public void testGetCheckedCount() {
        CategoryListItem item = new CategoryListItem(NAME, COUNT);
        assertEquals(item.getCheckedCount(), COUNT);
    }

    @Test
    public void testSetName() {
        CategoryListItem item = new CategoryListItem(NAME, COUNT);
        assertEquals(item.getName(), NAME);
        String otherName = "living room";
        item.setName(otherName);
        assertEquals(item.getName(), otherName);
    }

    @Test
    public void testSetCheckedCount() {
        CategoryListItem item = new CategoryListItem(NAME, COUNT);
        assertEquals(item.getCheckedCount(), COUNT);
        String otherCount = "10/10";
        item.setCheckedCount(otherCount);
        assertEquals(item.getCheckedCount(), otherCount);
    }
}
