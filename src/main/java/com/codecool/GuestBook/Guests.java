package com.codecool.GuestBook;

import com.codecool.GuestBook.helpers.MimeTypeResolver;
import com.codecool.GuestBook.model.DatabaseDataReader;
import com.codecool.GuestBook.model.XMLParser;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

public class Guests implements HttpHandler{

    public Guests() {
        DatabaseDataReader dataReader = new DatabaseDataReader();
        XMLParser xmlParser = new XMLParser();
        xmlParser.writeGuestsToXML(dataReader.getGuests().getIterator());
    }

    public void handle(HttpExchange httpExchange) throws IOException {

        // get file path from url
        URI uri = httpExchange.getRequestURI();

        String path = "." + uri.getPath();
        System.out.println("looking for: " + uri.getPath() + "\u001B[35m PATH: " + path + "\u001B[0m" );
        ClassLoader classLoader = getClass().getClassLoader();
        URL fileURL = classLoader.getResource(path);

        //OutputStream os = httpExchange.getResponseBody();

        if (fileURL == null) {
            // Object does not exist or is not a file: reject with 404 error.
            send404(httpExchange);
        } else {
            // Object exists and is a file: accept with response code 200.
            sendFile(httpExchange, fileURL);
        }

    }

    private void send404(HttpExchange httpExchange) throws IOException {
        String response = "404 (Not Found)\n";
        httpExchange.sendResponseHeaders(404, (long)response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }

    private void sendFile(HttpExchange httpExchange, URL fileURL) throws IOException {
        File file = new File(fileURL.getFile());
        System.out.println("fileURL: " + fileURL + " file: " + file);
        httpExchange.getResponseHeaders().set("Content-Type", "application/xml");
        httpExchange.sendResponseHeaders(200, file.length());
        OutputStream os = httpExchange.getResponseBody();
        FileInputStream fs = new FileInputStream(file);
        final byte[] buffer = new byte[0x10000];

        int count;
        while((count = fs.read(buffer)) >= 0) {
            System.out.println(buffer + " buffer || " + count + " count");
            os.write(buffer, 0, count);
        }

        os.close();
    }
}
