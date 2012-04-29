package com.shinetech.haloworld.chat;

import com.shinetech.haloworld.ChatMessage;

/**
 * Implemented by all type of chat participants.
 */
public class ChatMember {

    public String name;

    public ChatMember() {
    }

    public ChatMember(String name) {
        this.name = name;
    }

}
