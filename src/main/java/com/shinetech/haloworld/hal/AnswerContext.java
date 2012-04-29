package com.shinetech.haloworld.hal;

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

    public void couldNotAnswerQuestion(String resultId, String reason);

    public void log(String resultId, String message);
}
