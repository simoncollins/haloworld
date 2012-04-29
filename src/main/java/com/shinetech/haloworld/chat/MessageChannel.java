package com.shinetech.haloworld.chat;

import com.shinetech.haloworld.Message;

/**
 * Provides a means to send a message to a chat member.
 */
public interface MessageChannel {

    public void sendMessage(Message message);

    public String getName();
}
