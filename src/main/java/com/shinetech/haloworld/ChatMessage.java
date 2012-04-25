package com.shinetech.haloworld;

import javax.xml.bind.annotation.XmlRootElement;

import static com.shinetech.haloworld.MessageType.*;

/**
 * Stores a message sent to the chat session.
 */
@XmlRootElement
public class ChatMessage extends Message {

    public ChatMessage() {
        messageType = CHAT;
    }

    public ChatMessage(String messageText) {
        super(CHAT, messageText);
    }
}
