/*
 * Copyright 2012 Jeanfrancois Arcand
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.shinetech.haloworld;

import com.shinetech.haloworld.chat.AtmosphereMessageChannel;
import com.shinetech.haloworld.chat.ChatManager;
import com.shinetech.haloworld.chat.ChatMember;
import com.shinetech.haloworld.chat.MessageChannel;
import com.sun.jersey.api.core.InjectParam;
import org.atmosphere.annotation.Broadcast;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.jersey.Broadcastable;
import org.atmosphere.jersey.JerseyBroadcaster;
import org.atmosphere.jersey.SuspendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Main service end point for our chat application.
 */
@Path("/pubsub/chat")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Scope("singleton")
public class HaloworldChat {
    private static final Logger logger = LoggerFactory.getLogger(HaloworldChat.class);

    @InjectParam
    AtmosphereMessageChannel messageChannel;

    @InjectParam
    ChatManager chatManager;


//    private Broadcaster getBroadcaster() {
//        if(chatBroadcaster == null) {
//            BroadcasterFactory factory = BroadcasterFactory.getDefault();
//            chatBroadcaster = factory.get(JerseyBroadcaster.class, "chat");
//        }
//        return chatBroadcaster;
//    }

    /**
     * Called when a client wants to join the chat
     */
    @GET
    public SuspendResponse<Message> connect(@QueryParam("memberName") String memberName) {
        logger.info(memberName + " is joining the chat ....");
//        Executors.newSingleThreadExecutor().submit(new Runnable() {
//            public void run() {
//                while (true) {
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                    }
//                    logger.info("Broadcasting a ping!");
//                    getBroadcaster().broadcast("The server is pinging you at " + new Date().toString());
//                }
//            }
//        });

        ChatMember member = new ChatMember(memberName);

        chatManager.addChatMember(member, messageChannel);

        return new SuspendResponse.SuspendResponseBuilder<Message>()
                .broadcaster(messageChannel.getBroadcaster())
                .outputComments(true)
                .addListener(new EventsLogger())
                .build();
    }

    /**
     * Called when the client posts a message. The message will be broadcast to all clients
     * who have joined the chat.
     */
    @POST
//    @Broadcast()
    public void postMessage(@FormParam("message") String message,@FormParam("memberName") String memberName) {
//    public Broadcastable postMessage(@FormParam("message") String message) {

        logger.info("postMessage: {}", message);

        ChatMember member = chatManager.lookupMember(memberName);

        ChatMessage chatMessage = new ChatMessage(member, message);

        chatManager.postMessage(chatMessage);

//        LogMessage logMessage = new LogMessage(LogLevel.INFO, "message received and sent to chat manager");

//        return new Broadcastable(logMessage, "", messageChannel.getBroadcaster());
    }
}
