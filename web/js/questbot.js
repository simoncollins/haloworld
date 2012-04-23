$(document).ready(setup);

var socket = $.atmosphere;
var subSocket;

function setup() {
    // disable autocomplete on the message box
    $('#message-box').attr('autocomplete', 'OFF');

    // when the send button is clicked send the message to chat participants via the server
    $('#send-button').click(sendMessage);

    $('#clear-chat-button').click(clearChat);

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

function handleMessage(response) {
    console.log("handle message ....");
    if (response.status == 200) {
        detectedTransport = response.transport;
        var data = response.responseBody;
        console.log("Message received: " + data);
        $('#messages').append("<div class='message'>" + data + "</div>");
        // add message to the chat messages window
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
