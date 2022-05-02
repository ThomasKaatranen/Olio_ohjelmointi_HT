package com.example.courseproject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class FinnkinoReader {

    ArrayList<Event> eventList = new ArrayList<>();
    ArrayList<String> imageList = new ArrayList<>();

    private static FinnkinoReader fr = null;

    public static FinnkinoReader getInstance() {
        if (fr == null) {
            fr = new FinnkinoReader();
        }
        return fr;
    }

    private FinnkinoReader() {

    }

    //Read a .jpg image from finnkino XML file to arraylist
    public void readImageXML() {
        try {
            //DocumentBuilder builder = null;
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String urlString = "https://www.finnkino.fi/xml/Events/";
            Document doc = builder.parse(urlString);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getDocumentElement().getElementsByTagName("Images");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    if (element.getElementsByTagName("EventLargeImagePortrait").item(0) != null) {
                        imageList.add(i, element.getElementsByTagName("EventLargeImagePortrait").item(0).getTextContent());
                    }
                    else if (element.getElementsByTagName("EventSmallImageLandscape").item(0) != null) {
                        imageList.add(i, element.getElementsByTagName("EventSmallImageLandscape").item(0).getTextContent());
                    }
                    else {
                        imageList.add(i, "");
                    }

                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } finally {
            System.out.println("########DONE########");
        }
    }


    // Read events from finnkino XML to arraylist and return the list
    public ArrayList<Event> readEventsXML() {
        readImageXML();
        try {
            //DocumentBuilder builder = null;
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String urlString = "https://www.finnkino.fi/xml/Events/";
            Document doc = builder.parse(urlString);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getDocumentElement().getElementsByTagName("Event");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    eventList.add(i, new Event(element.getElementsByTagName("ID").item(0).getTextContent(),
                            element.getElementsByTagName("OriginalTitle").item(0).getTextContent(),
                            element.getElementsByTagName("LengthInMinutes").item(0).getTextContent(), imageList.get(i),
                            element.getElementsByTagName("Genres").item(0).getTextContent(),
                            element.getElementsByTagName("ShortSynopsis").item(0).getTextContent()));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } finally {
            System.out.println("########DONE########");
        }
        return eventList;
    }

}

