package com.shinetech.haloworld.hal.solver;

import com.shinetech.haloworld.TextData;
import com.shinetech.haloworld.hal.Answer;
import com.shinetech.haloworld.hal.AnswerContext;
import com.shinetech.haloworld.hal.QuestionSolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import static com.shinetech.haloworld.hal.Answer.TYPE.SIMPLE_TEXT_RESULT;

/**
 * Queries Wolfram Alpha to answer questions.
 */
public class WolframAlphaSolver implements QuestionSolver {
    private static final String API_KEY = "?";

    private final static Logger logger = LoggerFactory.getLogger(WolframAlphaSolver.class);

    String[] questions = new String[] {
        "how many (.*) in (.*)",
        "what is the (.*) of (.*)",
        "how many (.*) until (.*)",
        "when did (.*) live",
        "(.*) in (.*)",
        "convert (.*) to (.*)"
    };

    @Override
    public void provideAnswer(final String questionText, final AnswerContext context) {
        context.answeringQuestion();
        context.executeJob(new Runnable() {
            @Override
            public void run() {
                queryWolfram(questionText, context);
            }
        });
    }

    private void queryWolfram(final String question, final AnswerContext context) {
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            String encodedQuestion = URLEncoder.encode(question, "UTF-8");

            URL url = new URL("http://api.wolframalpha.com/v2/query?input=" + encodedQuestion + "&appid=" + API_KEY);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            System.out.println("Response code: " + responseCode);

            WolframHandler wolframHandler = new WolframHandler();
            parser.parse(conn.getInputStream(), wolframHandler);
            logger.debug("Success: " + wolframHandler.isSuccess() + " - Result: " + wolframHandler.getResult());

            if(wolframHandler.isSuccess()) {
                TextData data = new TextData(wolframHandler.getResult());
                Answer answer = new Answer(SIMPLE_TEXT_RESULT.name(), data);
                context.publishAnswer(answer);
            } else {
                logger.error("Error querying Wolfram Alpha");
                context.couldNotAnswerQuestion("There was an error querying Wolfram Alpha");
            }

        } catch(Exception e) {
            logger.error("Error querying Wolfram Alpha", e);
            context.couldNotAnswerQuestion("There was an error querying Wolfram Alpha");
        }
    }

    @Override
    public boolean canProvideAnswer(String questionText) {
        for(String question : questions) {
            if(questionText.matches(question)) {
                logger.debug("Matched: " + question);
                return true;
            } else {
                logger.debug("Did not match: " + question);
            }
        }
        logger.debug("Did not match any questions");
        return false;
    }

    public static class WolframHandler extends DefaultHandler {
        public static final String QUERY_ELEMENT = "queryresult";
        public static final String POD_ELEMENT = "pod";
        public static final String PLAINTEXT_ELEMENT = "plaintext";

        private enum STATE {
            START,
            IN_QUERY,
            IN_RESULT_POD,
            IN_RESULT_TEXT
        };

        private STATE currentState = STATE.START;

        private boolean isSuccess;
        private StringBuffer result = new StringBuffer();

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if(QUERY_ELEMENT.equals(qName)) {
                // in query element
                currentState = STATE.IN_QUERY;
                isSuccess = Boolean.parseBoolean(attributes.getValue("success"));
            } else if(POD_ELEMENT.equals(qName) && currentState == STATE.IN_QUERY && attributes.getValue("title").startsWith("Result")) {
                // in result POD
                currentState = STATE.IN_RESULT_POD;
            } else if(PLAINTEXT_ELEMENT.equals(qName) && currentState == STATE.IN_RESULT_POD) {
                currentState = STATE.IN_RESULT_TEXT;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if(currentState == STATE.IN_RESULT_TEXT && PLAINTEXT_ELEMENT.equals(qName)) {
                currentState = STATE.IN_RESULT_POD;
            } else if(currentState == STATE.IN_RESULT_POD && POD_ELEMENT.equals(qName)) {
                currentState = STATE.IN_QUERY;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if(currentState == STATE.IN_RESULT_TEXT) {
                result.append(ch, start, length);
            }
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public String getResult() {
            return result.toString();
        }
    }
}
