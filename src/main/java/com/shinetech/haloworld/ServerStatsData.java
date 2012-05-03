package com.shinetech.haloworld;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents server statistics
 */
@XmlRootElement(name = "data")
public class ServerStatsData extends Data {

    public String uptime;
    public String heapUsage;
    public float heapPercentageUsage;
    public String heapPercentageUsageString;

    public ServerStatsData() {
    }

    public ServerStatsData(String uptime, String heapUsage, float heapPercentageUsage, String heapPercentageUsageString) {
        this.uptime = uptime;
        this.heapUsage = heapUsage;
        this.heapPercentageUsage = heapPercentageUsage;
        this.heapPercentageUsageString = heapPercentageUsageString;
    }
}
