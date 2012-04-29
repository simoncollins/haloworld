package com.shinetech.haloworld;

import com.shinetech.haloworld.chat.ChatMember;

import javax.xml.bind.annotation.XmlRootElement;

import static com.shinetech.haloworld.MessageType.MEMBERJOINED;

/**
 * Sent by the chat manager when a new member joins the chat.
 */
@XmlRootElement
public class MemberJoinedMessage extends Message {

    public ChatMember[] chatMembers;
    public ChatMember newMember;

    public MemberJoinedMessage(ChatMember newMember, ChatMember[] chatMembers, String message) {
        super(MEMBERJOINED, message);
        this.chatMembers = chatMembers;
        this.newMember = newMember;
    }

}
