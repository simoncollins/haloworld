$(document).ready(setup);

var socket = $.atmosphere;
var subSocket;
var showLogs = true;
var memberName;

function setup() {
    // disable autocomplete on the message box
    $('#message-box').attr('autocomplete', 'OFF');

    // when the send button is clicked send the message to chat participants via the server
    $('#send-button').click(sendMessage);

    $('#clear-chat-button').click(clearChat);

    $('#show-log-messages-btn').click(toggleLogButtons);
    $('#hide-log-messages-btn').click(toggleLogButtons);

    $('#show-log-messages-btn').hide();

    $('#join-btn').click(handleLogin);

    $("#login-name").focus();

    $('#loginModal').on('shown', function () {
        this.focus();
    })

    $('#loginModal').modal({
        keyboard: false,
        backdrop: 'static'
    });

    $("#login-name").keypress(function(e){
        if (e.which == 13) {
            handleLogin();
            return false;
        }
        return true;
    });

    $("#message-box").keypress(function(e){
        if (e.which == 13) {
            sendMessage();
            return false;
        }
        return true;
    });



}

function handleLogin() {
    memberName = $('#login-name').val();
    if(memberName != null && memberName.length > 0) {
        $('#loginModal').modal('hide');
        connectToServer();
    }
    $('#message-box').focus();
    return false;
}

function sendMessage() {
    var message = $('#message-box').val();
    $('#message-box').val('');
    $('#message-box').focus();
    console.log("send button clicked: " + message);
    if(message) {
        console.log("Sending message: " + message);

        subSocket.push({
            data: 'message=' + message + '&memberName=' + memberName
        });
    }
    return false;
}

function clearChat() {
    $('#messages').empty();
    $('#message-box').focus();
    return false;
}

function toggleLogButtons() {
    $('#hide-log-messages-btn').toggle();
    $('#show-log-messages-btn').toggle();
    $('#feed .log').fadeToggle('fast', 'linear');
    showLogs = !showLogs;
    return false;
}

var LEVEL_TO_LABEL_MAP = {
    "DEBUG" : "inverse",
    "INFO" : "info",
    "WARNING" : "warning",
    "ERROR" : "important"
};

function addLogMessage(message) {
    var display = showLogs ? 'block' : 'none';

    var htmlContent = '<div class="result log" style="display:' + display + '"><span class="label label-' + LEVEL_TO_LABEL_MAP[message.level] + '">' +
        message.level + '</span> ' + message.messageText + '</div>';

    $('#feed').append(htmlContent);
}

function addChatMessage(message) {
    $('#messages').append("<div class='message'><b>" + message.sender.name + ": </b>" + message.messageText + "</div>");
}

function addFeedResult(message) {
    addFeedContainer(message.resultId);

    // create the result within the container
    FeedResultManager.createResult(message.resultType, message.resultId, message.data);

    $('#feed-' + message.resultId).effect("highlight", {}, 5000);
}

function addFeedContainer(resultId) {
    // create the feed container that the widget will render the result into
    var htmlContent = "<div id='feed-" + resultId + "' class='result feed'></div>"
    $('#feed').prepend(htmlContent);
}

function updateFeedResult(feedResultId, updateData) {
    FeedResultManager.updateResult(feedResultId, updateData);
}

function handleMessage(response) {
    if (response.status == 200) {

        var data = response.responseBody;
        var message = jQuery.parseJSON(data);

        switch(message.messageType) {
            case "LOG" :
                addLogMessage(message);
                break;
            case "CHAT" :
                addChatMessage(message);
                break;
            case "FEED_RESULT" :
                addFeedResult(message);
                break;
            case "FEED_RESULT_UPDATE" :
                updateFeedResult(message.resultId, message.data);
                break;
        }

    } else {
        console.error("Error receiving message: " + response.status + " - " + response.responseBody);
    }
}

function connectToServer() {

    // connection details
    var configUrl = document.location.toString() + 'atm/pubsub/chat?memberName=' + memberName;
    console.log("Connecting to URL: " + configUrl);

    var config = {
        url:        configUrl,
        transport:  'websocket',
        onMessage:  handleMessage
    };

    console.log("Connecting to server");

    subSocket = socket.subscribe(config);

    console.log("Connected to server");
}


var FeedResultManager = {

    resultWidgetConstructors: {},

    resultWidgets: {},

    registerWidgetConstructor: function(resultType, widgetConstructorFunction) {
        FeedResultManager.resultWidgetConstructors[resultType] = widgetConstructorFunction;
    },

    createResult: function(resultType, resultId, resultData) {
        var construct = FeedResultManager.resultWidgetConstructors[resultType];
        if(construct != null) {

            // create the widget that will render the result and tell it to render the initial result
            // store a reference to it so that we can feed it updates if/when they arrive.
            var resultWidget = construct(resultId);
            FeedResultManager.resultWidgets[resultId] = resultWidget;
            resultWidget.createWithData(resultData);
        } else {
            console.error("Could not create result of type '" + resultType + "' because no constructor function was found");
        }
    },

    updateResult: function(resultId, resultData) {
        var resultWidget = FeedResultManager.resultWidgets[resultId];
        if(resultWidget != null) {
            resultWidget.updateWithData(resultData);
        } else {
            console.error("Could not update widget with result ID '" + resultId + "' because it does not exist");
        }
    }
};
