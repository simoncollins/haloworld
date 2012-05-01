package com.shinetech.haloworld.hal;

import com.shinetech.haloworld.*;
import com.shinetech.haloworld.chat.ChatContext;
import com.shinetech.haloworld.chat.ChatMember;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.shinetech.haloworld.MessageType.CHAT;
import static com.shinetech.haloworld.MessageType.MEMBER_JOINED;

/**
 * HAL the intelligent chat bot.
 */
public class HAL {

    private ChatContext context;
    private static final String NAME = "HAL";
    private static final String HANDLE = "@" + NAME.toLowerCase();

    private List<QuestionSolver> questionSolvers = new ArrayList<QuestionSolver>();
    private ExecutorService executorService;
    private static final int THREADS = 10;

    public HAL() { }

    public HAL(List<QuestionSolver> questionSolvers) {
        this.questionSolvers.addAll(questionSolvers);
    }

    public String getChatName() {
        return NAME;
    }

    public void initWithContext(ChatContext context) {
        this.context = context;
        this.executorService = Executors.newFixedThreadPool(THREADS);
    }

    public void handleMessage(Message message) {
        if(message.messageType == CHAT) {
            ChatMessage chatMessage = (ChatMessage) message;
            handleChatMessage(chatMessage.sender, chatMessage.messageText);
        } else if(message.messageType == MEMBER_JOINED) {
            MemberJoinedMessage joinMessage = (MemberJoinedMessage) message;
            handleMemberJoined(joinMessage.newMember);
        }
    }

    private void handleChatMessage(ChatMember chatMember, String messageText) {

        String text = messageText.trim().toLowerCase();

        if(mentionedInMessage(text)) {
            // remove the @mention and any question mark
            text = text.replace(HANDLE, "").replace("?", "").trim();

            for(QuestionSolver questionSolver : questionSolvers) {
                if(questionSolver.canProvideAnswer(text)) {
                    questionSolver.provideAnswer(text, answerContext);
                    return;
                }
            }
            // if we are here we couldn't answer the question
            context.sendMessage(new ChatMessage("Sorry " + chatMember.name + ", I could not answer your question"));
        }
    }

    private void handleMemberJoined(ChatMember member) {
        context.sendMessage(new ChatMessage("Hello " + member.name + ", welcome to the chat ... I'm here to help"));
    }

    private boolean mentionedInMessage(String messageText) {
        return messageText.startsWith(HANDLE);
    }

    private String createResultId() {
        Long id = new Date().getTime();
        return id.toString();
    }

    private final AnswerContext answerContext = new AnswerContext() {
        @Override
        public String publishAnswer(Answer answer) {
            String resultId =  createResultId();
            FeedResultMessage message = new FeedResultMessage(resultId, answer.resultType, "Here is your answer", answer.data);
            context.sendMessage(message);
            return resultId;
        }

        @Override
        public void publishUpdate(String resultId, Answer answer) {
            FeedResultUpdateMessage message = new FeedResultUpdateMessage(resultId, answer.resultType, "", answer.data);
            context.sendMessage(message);
        }

        @Override
        public void couldNotAnswerQuestion(String reason) {
            context.sendMessage(new ChatMessage("Sorry, I could not answer your question"));
        }

        @Override
        public void answeringQuestion() {
            context.sendMessage(new ChatMessage("Looking up your answer, please wait ..."));
        }

        @Override
        public void log(String resultId, String message) {
            context.sendMessage(new LogMessage(LogLevel.INFO, "Result " + resultId + ": " + message));
        }

        @Override
        public void executeJob(Runnable job) {
            executorService.submit(job);
        }

        @Override
        public void executeJobRepeatedly(final int interval, final Runnable job) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    while(true) {
                        try {
                            job.run();
                            Thread.sleep(interval);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            });
        }
    };


}
