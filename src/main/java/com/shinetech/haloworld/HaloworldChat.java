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

import org.atmosphere.annotation.Broadcast;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.jersey.Broadcastable;
import org.atmosphere.jersey.SuspendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Simple PubSub resource that demonstrate many functionality supported by
 * Atmosphere JQuery Plugin and Atmosphere Jersey extension.
 *
 * @author Jeanfrancois Arcand
 */
@Path("/pubsub/{topic}")
@Produces("text/html;charset=ISO-8859-1")
public class HaloworldChat {
    private static final Logger logger = LoggerFactory.getLogger(HaloworldChat.class);


    private
    @PathParam("topic")
    Broadcaster topic;

    /**
     * Called when a client wants to join the chat
     */
    @GET
    public SuspendResponse<String> joinChat() {
        logger.info("Joining chat!!!!!");
//        Executors.newSingleThreadExecutor().submit(new Runnable() {
//            public void run() {
//                while (true) {
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                    }
//                    logger.info("Broadcasting a ping!");
//                    topic.broadcast("The server is pinging you at " + new Date().toString());
//                }
//            }
//        });

        return new SuspendResponse.SuspendResponseBuilder<String>()
                .broadcaster(topic)
                .outputComments(true)
                .addListener(new EventsLogger())
                .build();
    }

    /**
     * Called when the client posts a message. The message will be broadcast to all clients
     * who have joined the chat.
     */
    @POST
    @Broadcast()
    public Broadcastable postMessage(@FormParam("message") String message) {

        logger.info("postMessage: {}", message);


        return new Broadcastable(message, "", topic);
    }
}
