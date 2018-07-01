package com.codecool.GuestBook.model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public Connection connect(String password) {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/guestbook",
                            "szymon", password);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return c;
    }
}
