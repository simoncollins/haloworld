package com.shinetech.haloworld.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Simple runner class to make calls to Wolfram Alpha API.
 */
public class WolframRunner {

    public static void main(String[] args) throws Exception {

        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

        URL url = new URL("http://api.wolframalpha.com/v2/query?input=100%20ounces%20in%20kilograms&appid=XXXX");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        int responseCode = conn.getResponseCode();
        System.out.println("Response code: " + responseCode);

        WolframHandler wolframHandler = new WolframHandler();
        parser.parse(conn.getInputStream(), wolframHandler);
        System.out.println("Success: " + wolframHandler.isSuccess() + " - Result: " + wolframHandler.getResult());
    }

    private static class WolframHandler extends DefaultHandler {
        public static final String QUERY_ELEMENT = "queryresult";
        public static final String POD_ELEMENT = "pod";
        public static final String PLAINTEXT_ELEMENT = "plaintext";

        private enum STATE {
           START,
           IN_QUERY,
           IN_RESULT_POD,
           IN_RESULT_TEXT
        };

        private STATE currentState = STATE.START;

        private boolean isSuccess;
        private StringBuffer result = new StringBuffer();

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if(QUERY_ELEMENT.equals(qName)) {
                // in query element
                currentState = STATE.IN_QUERY;
                isSuccess = Boolean.parseBoolean(attributes.getValue("success"));
            } else if(POD_ELEMENT.equals(qName) && currentState == STATE.IN_QUERY && attributes.getValue("title").equals("Result")) {
                // in result POD
                currentState = STATE.IN_RESULT_POD;
            } else if(PLAINTEXT_ELEMENT.equals(qName) && currentState == STATE.IN_RESULT_POD) {
                currentState = STATE.IN_RESULT_TEXT;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if(currentState == STATE.IN_RESULT_TEXT && PLAINTEXT_ELEMENT.equals(qName)) {
                currentState = STATE.IN_RESULT_POD;
            } else if(currentState == STATE.IN_RESULT_POD && POD_ELEMENT.equals(qName)) {
                currentState = STATE.IN_QUERY;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if(currentState == STATE.IN_RESULT_TEXT) {
                result.append(ch, start, length);
            }
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public String getResult() {
            return result.toString();
        }
    }
}
