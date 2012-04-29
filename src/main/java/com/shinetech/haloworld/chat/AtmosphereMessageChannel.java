package com.shinetech.haloworld.chat;

import com.shinetech.haloworld.Message;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.jersey.JerseyBroadcaster;

/**
 * Message channel that sends messages via an atmosphere broadcaster
 */
public class AtmosphereMessageChannel implements MessageChannel {

    private final String name = "Atmosphere Message Channel";

    private Broadcaster broadcaster;


    @Override
    public void sendMessage(Message message) {
        getBroadcaster().broadcast(message);
    }

    public Broadcaster getBroadcaster() {
        if(broadcaster == null) {
            BroadcasterFactory factory = BroadcasterFactory.getDefault();
            broadcaster = factory.get(JerseyBroadcaster.class, "chat");
        }
        return broadcaster;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AtmosphereMessageChannel that = (AtmosphereMessageChannel) o;

        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public String getName() {
        return name;
    }
}
