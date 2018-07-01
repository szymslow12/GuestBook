package com.codecool.GuestBook.model;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Iterator;

public class XMLParser {

    private Document doc;

    public XMLParser() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            this.doc = docBuilder.newDocument();
        } catch(ParserConfigurationException err) {
            err.printStackTrace();
            System.exit(0);
        }
    }

    public void writeGuestsToXML(Iterator<Guest> guestIterator) {
        Element rootElem = doc.createElement("guests");
        doc.appendChild(rootElem);
        while (guestIterator.hasNext()) {
            rootElem.appendChild(getGuest(guestIterator.next()));
        }
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("src/main/resources/static/guestbook.xml"));

            // Output to console for testing
            // StreamResult terminalResult = new StreamResult(System.out);

            transformer.transform(source, result);
            System.out.println("Guestbook is created.");
        } catch(TransformerException err) {
            err.printStackTrace();
            System.exit(0);
        }

    }


    private Element getGuest(Guest guest) {
        Element xmlGuest = doc.createElement("guest");
        xmlGuest.setAttribute("id_guest", Integer.toString(guest.getGuestID()));
        Element name = doc.createElement("guest_name");
        name.setTextContent(guest.getName());
        xmlGuest.appendChild(name);
        Element message = doc.createElement("message");
        message.setTextContent(guest.getMessage());
        xmlGuest.appendChild(message);
        Element date = doc.createElement("visit_date");
        date.setTextContent(guest.getDate());
        xmlGuest.appendChild(date);
        return xmlGuest;
    }
}
