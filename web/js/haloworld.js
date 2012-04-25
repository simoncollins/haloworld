$(document).ready(setup);

var socket = $.atmosphere;
var subSocket;

function setup() {
    // disable autocomplete on the message box
    $('#message-box').attr('autocomplete', 'OFF');

    // when the send button is clicked send the message to chat participants via the server
    $('#send-button').click(sendMessage);

    $('#clear-chat-button').click(clearChat);

    $('#show-log-messages-btn').click(toggleLogButtons);
    $('#hide-log-messages-btn').click(toggleLogButtons);

    $('#show-log-messages-btn').hide();

    connectToServer();
}

function sendMessage() {
    var message = $('#message-box').val();
    $('#message-box').val('');
    $('#message-box').focus();
    console.log("send button clicked: " + message);
    if(message) {
        console.log("Sending message: " + message);

        subSocket.push({
            data: 'message=' + message
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
    return false;
}

var LEVEL_TO_LABEL_MAP = {
    "DEBUG" : "inverse",
    "INFO" : "info",
    "WARNING" : "warning",
    "ERROR" : "important"
};

function logMessage(level, message) {
    var htmlContent = '<div class="result log"><span class="label label-' + LEVEL_TO_LABEL_MAP[level] + '">' +
        level + '</span> ' + message + '</div>';

    $('#feed').append(htmlContent);
}

function handleMessage(response) {
    console.log("handle message ....");
    if (response.status == 200) {

        var data = response.responseBody;

        console.log("Message received: " + data);
        var message = jQuery.parseJSON(data);

        // add message to the chat messages window
        $('#messages').append("<div class='message'>" + message.messageText + "</div>");

        if(message.messageType == "LOG") {
            logMessage(message.level, message.messageText);
        }

    } else {
        console.error("Error receiving message: " + response.status + " - " + response.responseBody);
    }
}

function connectToServer() {

    // connection details
    var configUrl = document.location.toString() + 'atm/pubsub/chat';
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
