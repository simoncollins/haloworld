package com.shinetech.haloworld.util;

import com.shinetech.haloworld.hal.solver.WolframAlphaSolver;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Simple runner class to make calls to Wolfram Alpha API.
 */
public class WolframRunner {

    public static void main(String[] args) throws Exception {
        String question = URLEncoder.encode(args[0], "UTF-8");
        System.out.println("Question: " + args[0]);
        System.out.println("Encoded Question: " + question);

        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

        URL url = new URL("http://api.wolframalpha.com/v2/query?input=" + question + "&appid=XX8QPW-XX");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        int responseCode = conn.getResponseCode();
        System.out.println("Response code: " + responseCode);

        WolframAlphaSolver.WolframHandler wolframHandler = new WolframAlphaSolver.WolframHandler();
        parser.parse(conn.getInputStream(), wolframHandler);
        System.out.println("Success: " + wolframHandler.isSuccess() + " - Result: " + wolframHandler.getResult());
    }


}
