package com.shinetech.haloworld;

import javax.xml.bind.annotation.XmlRootElement;

import static com.shinetech.haloworld.LogLevel.*;
import static com.shinetech.haloworld.MessageType.*;

/**
 * Represents a log message that can have a logging level to indicate severity.
 */
@XmlRootElement
public class LogMessage extends Message {

    public LogLevel level = DEBUG;

    public LogMessage(String messageText) {
        super(LOG, messageText);
    }

    public LogMessage(LogLevel level, String messageText) {
        super(LOG, messageText);
        this.level = level;
    }

    public LogMessage() {
    }
}
