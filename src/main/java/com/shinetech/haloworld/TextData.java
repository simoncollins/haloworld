package com.shinetech.haloworld;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents text data (until I can figure out how to get simple Strings rendering to JSON Strings).
 */
@XmlRootElement(name = "data")
public class TextData extends Data {
    public String text;

    public TextData() {}

    public TextData(String text) {
        this.text = text;
    }
}
