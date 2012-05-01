package com.shinetech.haloworld.hal;

import java.util.concurrent.ExecutorService;

/**
 * Context that a QuestionSolver can use to publish answers to questions and updates to those answers
 */
public interface AnswerContext {

    /**
     * Publishes the initial answer to the question
     * @param answer The initial answer.
     * @return The result ID that should be referred to in updates.
     */
    public String publishAnswer(Answer answer);

    public void publishUpdate(String resultId, Answer answer);

    public void couldNotAnswerQuestion(String reason);

    public void answeringQuestion();

    public void log(String resultId, String message);

    public void executeJob(Runnable job);
    public void executeJobRepeatedly(int interval, Runnable job);
}
