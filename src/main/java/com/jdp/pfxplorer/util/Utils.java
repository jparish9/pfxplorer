package com.jdp.pfxplorer.util;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URL;

/**
 * Miscellaneous utility methods.
 */
public class Utils {
    /**
     * Parse an xml string into a Document for querying.
     */
    public static Document parseXml(String xml) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(new InputSource(new StringReader(xml)));
    }

    /**
     * Helper method to read a url directly into a string.
     * @return contents of url as a string, or null if there was an error.
     */
    public static String readUrl(String urlString) {
        InputStream urlStream;
        try {
            urlStream = new URL(urlString).openStream();
        } catch (IOException e) {
            return null;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(urlStream));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                urlStream.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }
}
