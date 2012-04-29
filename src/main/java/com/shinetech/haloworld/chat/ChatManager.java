package com.shinetech.haloworld.chat;

import com.shinetech.haloworld.Message;
import com.shinetech.haloworld.hal.HAL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Manages the list of chat members and provides a means of adding messages to the conversation.
 * In the interests of simplicity, this chat manager knows about HAL and passes him any messages
 * in which he is mentioned.
 */
public class ChatManager implements ChatContext {

    private static final Logger logger = LoggerFactory.getLogger(ChatManager.class);

    private Map<String, ChatMember> chatMembers = new HashMap<String, ChatMember>();
    private Set<MessageChannel> messageChannels = new HashSet<MessageChannel>();

    private HAL halBot;

    public ChatManager(HAL halBot) {
        this.halBot = halBot;
        init();
    }

    private void init() {

        // add HAL as our first member
        String name = halBot.getChatName();
        chatMembers.put(name, new ChatMember(name));
        halBot.initWithContext(this);
    }

    /**
     * Adds a new participant into the chat and provides a channel on which they can have messages sent.
     * Each chat message is only sent once to each unique channel as a channel may have more than one member
     * listening on it.
     */
    public synchronized void addChatMember(ChatMember member, MessageChannel messageChannel) {
        chatMembers.put(member.name, member);
        messageChannels.add(messageChannel);
    }

    /**
     * Post a message into the chat conversation.
     * @param message The message to post to everyone in the chat.
     */
    public void postMessage(Message message) {
        postMessageToChannels(message);

        // let HAL know about the message
        halBot.handleMessage(message);
    }

    private void postMessageToChannels(Message message) {
        for(MessageChannel channel : messageChannels) {
            logger.debug("Sending message to message channel <" + channel.getName() + ">");
            channel.sendMessage(message);
        }
    }

    public ChatMember lookupMember(String memberName) {
        return chatMembers.get(memberName);
    }


    @Override
    public void sendMessage(Message message) {
        // mark message as coming from HAL
        ChatMember hal = lookupMember(halBot.getChatName());
        message.sender = hal;
        postMessageToChannels(message);
    }
}
