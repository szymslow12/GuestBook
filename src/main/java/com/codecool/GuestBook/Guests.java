package com.codecool.GuestBook;


import com.codecool.GuestBook.model.Guest;
import com.codecool.GuestBook.model.GuestDAO;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;

import java.util.Iterator;

public class Guests implements HttpHandler {

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = getResponse();

        httpExchange.getResponseHeaders().set("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, response.length());

        OutputStream os = httpExchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }

    private String getResponse() {
        GuestDAO dataReader = new GuestDAO();
        Iterator<Guest> guestIterator = dataReader.getGuests().getIterator();
        JSONArray array = new JSONArray();
        while (guestIterator.hasNext()) {
            Guest guest = guestIterator.next();
            JSONObject object = new JSONObject();
            object.put("name", guest.getName());
            object.put("message", guest.getMessage());
            object.put("date", guest.getDate());
            array.put(object);
        }
        return array.toString();
    }
}