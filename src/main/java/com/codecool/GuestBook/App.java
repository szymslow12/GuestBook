package com.codecool.GuestBook;

import com.codecool.GuestBook.controller.GuestBook;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/guests", new Guests());

        server.createContext("/static", new Static());
        server.createContext("/guestBook", new GuestBook());
        server.setExecutor(null);

        server.start();
    }
}
