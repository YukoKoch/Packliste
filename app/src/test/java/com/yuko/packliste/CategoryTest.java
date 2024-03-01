package com.yuko.packliste;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
public class CategoryTest {
    @Test
    public void testGetName() {
        String room = "bedroom";
        Category category = new Category(room);
        assertEquals(category.getName(), room);
    }

    @Test
    public void testSetName() {
        String room = "bedroom";
        Category category = new Category(room);
        assertEquals(category.getName(), room);
        String otherRoom = "living room";
        category.setName(otherRoom);
        assertEquals(category.getName(), otherRoom);
    }
}
