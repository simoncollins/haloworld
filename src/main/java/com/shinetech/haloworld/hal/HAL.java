package com.shinetech.haloworld.hal;

import com.shinetech.haloworld.ChatMessage;
import com.shinetech.haloworld.MemberJoinedMessage;
import com.shinetech.haloworld.Message;
import com.shinetech.haloworld.MessageType;
import com.shinetech.haloworld.chat.ChatContext;
import com.shinetech.haloworld.chat.ChatMember;

import static com.shinetech.haloworld.MessageType.CHAT;
import static com.shinetech.haloworld.MessageType.MEMBERJOINED;

/**
 * HAL the intelligent chat bot.
 */
public class HAL {

    private ChatContext context;

    public HAL() { }

    public String getChatName() {
        return "HAL";
    }

    public void initWithContext(ChatContext context) {
        this.context = context;
    }

    public void handleMessage(Message message) {
        if(message.messageType == CHAT) {
            ChatMessage chatMessage = (ChatMessage) message;
            handleChatMessage(chatMessage.sender, chatMessage.messageText);
        } else if(message.messageType == MEMBERJOINED) {
            MemberJoinedMessage joinMessage = (MemberJoinedMessage) message;
            handleMemberJoined(joinMessage.newMember);
        }
    }

    private void handleChatMessage(ChatMember chatMember, String messageText) {
        context.sendMessage(new ChatMessage(chatMember.name + ", I heard you say '" + messageText + "'"));
    }

    private void handleMemberJoined(ChatMember member) {
        context.sendMessage(new ChatMessage("Hello " + member.name + ", welcome to the chat ... I'm here to help"));
    }
}
