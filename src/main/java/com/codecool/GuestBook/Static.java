package com.codecool.GuestBook;

import com.codecool.GuestBook.helpers.MimeTypeResolver;
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

public class Static implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        // get file path from url
        URI uri = httpExchange.getRequestURI();

        String path = "." + uri.getPath();
        System.out.println("looking for: " + uri.getPath() + "\u001B[35m PATH: " + path + "\u001B[0m" );
        // get file from resources folder, see: https://www.mkyong.com/java/java-read-a-file-from-resources-folder/
        ClassLoader classLoader = getClass().getClassLoader();
        URL fileURL = classLoader.getResource(path);

        //OutputStream os = httpExchange.getResponseBody();

        if (fileURL == null) {
            // Object does not exist or is not a file: reject with 404 error.
            send404(httpExchange);
        } else {
            // Object exists and is a file: accept with response code 200.
            sendFile(httpExchange, fileURL);
            //sendHTMLFile(httpExchange, fileURL);
        }

    }

    private void send404(HttpExchange httpExchange) throws IOException {
        String response = "404 (Not Found)\n";
        httpExchange.sendResponseHeaders(404, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }

    private void sendFile(HttpExchange httpExchange, URL fileURL) throws IOException {
        // get the file
        File file = new File(fileURL.getFile());
        // we need to find out the mime type of the file, see: https://en.wikipedia.org/wiki/Media_type
        MimeTypeResolver resolver = new MimeTypeResolver(file);
        System.out.println("fileURL: " + fileURL + " file: " + file);
        String mime = resolver.getMimeType();
        System.out.println(" mime resolver.getMimeType()");

        //httpExchange.getResponseHeaders().set("Content-Type", mime);
        httpExchange.sendResponseHeaders(200, file.length());

        OutputStream os = httpExchange.getResponseBody();

        // send the file
        FileInputStream fs = new FileInputStream(file);
        final byte[] buffer = new byte[0x10000];
        int count = 0;
        while ((count = fs.read(buffer)) >= 0) {
            System.out.println(buffer + " buffer || " + count + " count");
            os.write(buffer,0,count);
        }
        os.close();
    }

    private void checkFile(File file) {
        if (file.isFile()) {
            System.out.println("\u001B[31m File is a file \u001B[0m");
        } else if (file.isDirectory()) {
            System.out.println("\u001B[31m File is a directory \u001B[0m");
            checkFilesInDirectory(file);
        }
    }

    private void checkFilesInDirectory(File file) {
        String[] filesInDirectory = file.list();
        System.out.println("\u001B[31m File in directory:");
        for (int i = 0; i < filesInDirectory.length; i++) {
            System.out.println(i + ". " + filesInDirectory[i] + "\u001B[0m");
        }
    }

    private void sendHTMLFile(HttpExchange httpExchange, URL fileURL) throws IOException {
        sendFile(httpExchange, getHTMLFileURL(fileURL));
        //sendFile(httpExchange, getPathToCSS(fileURL));

    }


    private URL getHTMLFileURL(URL fileURL) {
        File file = new File(fileURL.getFile());
        //checkFile(file);
        try {
            return new URL(getAbsolutePathToFile(file, fileURL.toString(), ".html"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.println("error");
        return fileURL;
    }

    private String getAbsolutePathToFile(File file, String fileURL, String matcher) {
        String[] filesInDirectory = file.list();
        for (int i = 0; i < filesInDirectory.length; i++) {
            if (filesInDirectory[i].contains(matcher)) {
                return fileURL + "/" + filesInDirectory[i];
            }
        }
        return fileURL;
    }

    private URL getPathToCSS(URL fileURL) {
        String filePath = fileURL.getFile();
        File file = new File(filePath);
        String path = getAbsolutePathToFile(file, fileURL.toString(), "css");
        System.out.println(path + "    css");
        try {
            String pathToCSS = getAbsolutePathToFile(new File(new URL(path).getFile()), path, ".css");
            System.out.println(pathToCSS + " zadzialaj");
            return new URL(pathToCSS);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.println("error");
        return fileURL;
    }
}

