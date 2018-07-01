package com.codecool.GuestBook.model;

public class Guest {

    private int guestID;
    private String name;
    private String message;
    private String date;

    public Guest(int guestID, String name, String message, String date) {
        this.guestID = guestID;
        this.name = name;
        this.message = message;
        this.date = date;
    }


    public int getGuestID() {
        return guestID;
    }


    public String getName() {
        return name;
    }


    public String getMessage() {
        return message;
    }


    public String getDate() {
        return date;
    }


    public String toString() {
        return String.format("ID: %s Name: %s", guestID, name);
    }
}
