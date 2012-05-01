package com.shinetech.haloworld.hal.solver;

import com.shinetech.haloworld.ServerStatsData;
import com.shinetech.haloworld.TextData;
import com.shinetech.haloworld.hal.Answer;
import com.shinetech.haloworld.hal.AnswerContext;
import com.shinetech.haloworld.hal.QuestionSolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.RuntimeMXBean;

import static com.shinetech.haloworld.hal.Answer.TYPE.SERVER_STATS_RESULT;
import static com.shinetech.haloworld.hal.Answer.TYPE.SIMPLE_TEXT_RESULT;

/**
 * Knows how to answer questions about server statistics
 */
public class ServerStatsQuestionSolver implements QuestionSolver {
    private final static Logger logger = LoggerFactory.getLogger(ServerStatsQuestionSolver.class);

    private final int POLLING_INTERVAL = 4000;

    String[] questions = new String[] {
            "how is the server doing",
            "how the server is doing",
            "how is the server performance",
            "show me the server stats",
            "show me some server stats",
            "show me the stats",
            "how are the stats",
            "how are the server stats"
    };

    @Override
    public void provideAnswer(String questionText, final AnswerContext context) {

        Answer answer = new Answer(SERVER_STATS_RESULT.name(), getServerStats());
        String resultId = context.publishAnswer(answer);

        scheduleUpdates(resultId, context);
    }

    @Override
    public boolean canProvideAnswer(String questionText) {
        for(String question : questions) {
            if(questionText.contains(question)) {
                return true;
            }
        }
        return false;
    }

    private ServerStatsData getServerStats() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

        Long uptime = runtimeMXBean.getUptime();

        MemoryUsage usage = memoryMXBean.getHeapMemoryUsage();
        long maxMem = usage.getMax();
        long usedMem = usage.getUsed();
        String heapUsageAsString = Long.toString(usedMem);
        float heapPercentageUsage = 100 * ((float) usedMem/maxMem);

        return new ServerStatsData(uptime.toString(), heapUsageAsString, heapPercentageUsage);
    }

    private void scheduleUpdates(final String resultId, final AnswerContext context) {
        context.log(resultId, "Scheduling regular stats updates for this question");

        // schedule regular execution of stats polling
        context.executeJobRepeatedly(POLLING_INTERVAL, new Runnable() {
            @Override
            public void run() {
                logger.debug("Updating statistics");
                Answer answer = new Answer(SERVER_STATS_RESULT.name(), getServerStats());
                context.log(resultId, "Sent updates stats");
                context.publishUpdate(resultId, answer);
            }
        });
    }
}
