package com.codecool.GuestBook.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

public class DatabaseDataReader {

    private Connection connection;
    private GuestRepository guests;

    public DatabaseDataReader() {
        connection = new DatabaseConnection().connect(" ");
        guests = new GuestRepository();
        loadGuestsFromDatabase();
    }


    private void loadGuestsFromDatabase() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("Select * from guests");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                guests.addGuest(getGuestFromDatabse(resultSet));
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }


    private Guest getGuestFromDatabse(ResultSet resultSet) throws SQLException{
        int guestID = resultSet.getInt("id_guest");
        String name = resultSet.getString("guest_name");
        String message = resultSet.getString("message");
        String date = resultSet.getString("visit_date");
        return new Guest(guestID, name, message, date);
    }


    private void showGuests() {
        Iterator<Guest> guestIterator = guests.getIterator();
        while(guestIterator.hasNext()) {
            System.out.println(guestIterator.next());
        }
    }


    public GuestRepository getGuests() {
        return guests;
    }
}
