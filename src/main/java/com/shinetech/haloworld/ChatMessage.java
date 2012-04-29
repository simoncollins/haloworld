package com.shinetech.haloworld;

import com.shinetech.haloworld.chat.ChatMember;

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

    public ChatMessage(ChatMember sender, String messageText) {
        super(CHAT, messageText);
        this.sender = sender;
    }

    public ChatMessage(String messageText) {
        super(CHAT, messageText);
    }
}
