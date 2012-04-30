package com.shinetech.haloworld;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import java.util.Date;

import static com.shinetech.haloworld.MessageType.*;

/**
 * Message with feed information
 */
@XmlRootElement
@XmlSeeAlso({TextData.class, ServerStatsData.class})
public class FeedResultMessage extends Message {

    public Data data;
    public String resultId;
    public String resultType;

    public FeedResultMessage() {
    }

    public FeedResultMessage(String resultId, String resultType, String messageText, Data data) {
        super(FEED_RESULT, messageText);
        this.data = data;
        this.resultId = resultId;
        this.resultType = resultType;
    }

}
