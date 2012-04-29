package com.shinetech.haloworld;

import javax.xml.bind.annotation.XmlRootElement;

import static com.shinetech.haloworld.MessageType.FEED_RESULT_UPDATE;

/**
 * Message containing updated data for a feed result.
 */
@XmlRootElement
public class FeedResultUpdateMessage extends FeedResultMessage {

    public FeedResultUpdateMessage() {
    }

    public FeedResultUpdateMessage(String resultId, String resultType, String messageText, Data data) {
        super(resultId, resultType, messageText, data);
        this.messageType = FEED_RESULT_UPDATE;
    }
}
