/**
 * Sends the message contained within the message box (provided there is one) from the
 * given sender to the given receiver.
 * @param matchID The unique identifier for the match. Used to send messages to specific a server sent event handler
 * @param sender The twitter name of the sender
 */
function sendMessage(matchID, sender) {
    var message = document.getElementById("messageBox").value;

    if(message != null && message != "" && matchID != null && matchID != "" && sender != null && sender != "") {
        var msg = { _id: "", matchID: matchID, sender: sender, message: message, dateTime: (new Date()).getTime() };

        jsRoutes.controllers.MessagingController.sendMessage().ajax({
            data: JSON.stringify(msg),
            dataType: 'json',
            contentType: 'application/json',
            type: 'post'
        });

        document.getElementById("messageBox").value = "";
    }
}

/**
 * Adds a message to the message feed
 * @param message The message to add
 * @param senderTwitterName The name of the message sender
 */
function addMessageDiv(message, senderTwitterName) {
    if(message != null) {
        var newMessage = document.createElement("div");
        newMessage.className = "full-width outerMessageBox";

        if (message.sender == senderTwitterName) {
            newMessage.innerHTML = "<span class='message-box-current-user'><p>" + message.message + "</p></span>";
        } else {
            newMessage.innerHTML = "<span class='message-box-other-user'><p>" + message.message + "</p></span>";
        }

        document.getElementById("messageFeed").appendChild(newMessage);
    }
}

/**
 * Loads previous messages based on the match identifier
 * @param matchID The match to load messages for
 * @param successCallback Overrides the success callback (should only be used for testing)
 * @param errorCallback Overrides the error callback (should only be used for testing)
 */
function loadMoreMessages(matchID, senderTwitterName, successCallback, errorCallback) {
    if(matchID != null && matchID != "") {
        jsRoutes.controllers.MessagingController.getMoreMessages(matchID).ajax({
            success: successCallback || function(result) { addMoreMessages(result, senderTwitterName) },
            error: errorCallback || failedToLoadMoreMessages
        });
    }
}

/**
 * Add more messages to the page
 * @param messageList The messages to add
 */
function addMoreMessages(messageList, senderTwitterName) {
    if(messageList != null && messageList != null) {
        for (var i = 0; i < messageList.length; i++) {
            addMessageDiv(messageList[i], senderTwitterName);
        }

        var messageDiv = document.getElementById('messageFeed');
        messageDiv.scrollTop = messageDiv.scrollHeight;
    }
}

/**
 * Handles the lack of more messages
 * @param error
 */
function failedToLoadMoreMessages(error) {
    //Nothing to do
}

/**
 * Resizes the messaging box to fill the majority of the page
 */
function doResize() {
    var height = $(window).height() - $('#header').height() - $('#sendMessage').height() - 150;
    if(height < $(window).height() * 0.2)
        height = $(window).height() * 0.2;

    $('#messageFeed').height(height);
}