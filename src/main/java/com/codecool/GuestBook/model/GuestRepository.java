package com.codecool.GuestBook.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuestRepository {

    private List<Guest> guestList;

    public GuestRepository() {
        this.guestList = new ArrayList<>();
    }

    public void addGuest(Guest guest) {
        guestList.add(guest);
    }

    public Guest getGuest(int index) {
        return guestList.get(index);
    }

    public Iterator<Guest> getIterator() {
        return guestList.iterator();
    }
}
