package com.codecool.GuestBook.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

public class GuestDAO {

    private Connection connection;
    private GuestRepository guests;

    public GuestDAO() {
        connection = new DatabaseConnection().connect(" ");
        guests = new GuestRepository();
        loadGuestsFromDatabase();
    }


    private void loadGuestsFromDatabase() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("Select * FROM guests ORDER BY id_guest");
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

    public void sendNewGuestToDatabase(Map<String, Object> guest) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO guests(guest_name, message, visit_date) VALUES(?, ? ,?)");
            for (int i = 0; i < guest.keySet().size(); i++) {
                String key = (String) guest.keySet().toArray()[i];
                preparedStatement.setString(i + 1, (String) guest.get(key));
            }
            preparedStatement.setString(3, LocalDate.now().toString() + " " + LocalTime.now().toString());
            preparedStatement.execute();
        } catch (SQLException err) {
            err.printStackTrace();
        }

    }
}
