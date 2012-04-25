package com.shinetech.haloworld;

import javax.xml.bind.annotation.XmlRootElement;
import static com.shinetech.haloworld.MessageType.*;

/**
 * Represents a message sent to clients. The messageType field indicates the type of message (feed, log, chat etc.)
 */
@XmlRootElement
public class Message {

    public MessageType messageType;

    public String messageText;

    public Message() {

    }

    public Message(MessageType messageType, String messageText) {
        this.messageType = messageType;
        this.messageText = messageText;
    }
}
