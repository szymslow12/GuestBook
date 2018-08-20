package com.codecool.GuestBook.controller;

import com.codecool.GuestBook.model.GuestDAO;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;

public class GuestBookController implements HttpHandler {
    String response;
    HttpExchange httpExchange;

    @Override
    public void handle(HttpExchange httpExchange) {

        this.httpExchange = httpExchange;
        String method = httpExchange.getRequestMethod();

        try {
            if (method.equalsIgnoreCase("GET")) {
                buildIndexHtml();
                sendResponse();
            } else {
                receiveGuestFromSite();
                buildIndexHtml();
                sendResponse();
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private void receiveGuestFromSite() throws IOException {
        Map<String, Object> parameters = new HashMap<>();
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        parseQuery(query, parameters);
        GuestDAO guestDAO = new GuestDAO();
        guestDAO.sendNewGuestToDatabase(parameters);
    }

    private void sendResponse() throws IOException
    {
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void buildIndexHtml() throws FileNotFoundException
    {
        Scanner scanner = new Scanner(new File("src/main/resources/static/index.html"));
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine()).append("\n");
        }
        response = stringBuilder.toString();
        scanner.close();
    }

    public static void parseQuery(String query, Map<String,
            Object> parameters) throws UnsupportedEncodingException {

        if (query != null) {
            String pairs[] = query.split("[&]");
            for (String pair : pairs) {
                String param[] = pair.split("[=]");
                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0],
                            System.getProperty("file.encoding"));
                }

                if (param.length > 1) {
                    value = URLDecoder.decode(param[1],
                            System.getProperty("file.encoding"));
                }

                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if (obj instanceof List<?>) {
                        List<String> values = (List<String>) obj;
                        values.add(value);

                    } else if (obj instanceof String) {
                        List<String> values = new ArrayList<String>();
                        values.add((String) obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }
}
