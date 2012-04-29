package com.shinetech.haloworld.chat;

import com.shinetech.haloworld.Message;

/**
 * Represents the context in which a chat is taking place.
 */
public interface ChatContext {

    public void sendMessage(Message message);
}
