package com.shinetech.haloworld;

import javax.xml.bind.annotation.XmlRootElement;

import static com.shinetech.haloworld.MessageType.*;

/**
 * Message with feed information
 */
@XmlRootElement
public class FeedMessage extends Message {

    public FeedMessage() {
        messageType = FEED;
    }

    public FeedMessage(String messageText) {
        super(FEED, messageText);
    }
}
