/**
 * Sends the message contained within the message box (provided there is one) from the
 * given sender to the given receiver.
 * @param matchID The unique identifier for the match. Used to send messages to specific a server sent event handler
 * @param sender The twitter name of the sender
 */
function sendMessage(matchID, sender) {
    var message = document.getElementById("messageBox").value;

    if(message != null && message != "") {
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
 * Loads previous messages based on the match identifier
 * @param matchID The match to load messages for
 */
function loadMoreMessages(matchID, senderTwitterName) {
    if(matchID != null && matchID != "") {
        jsRoutes.controllers.MessagingController.getMoreMessages(matchID).ajax({
            success: function(result) { addMoreMessages(result, senderTwitterName) },
            error: failedToLoadMoreMessages
        });
    }
}

/**
 * Add more messages to the page
 * @param messageList The messages to add
 */
function addMoreMessages(messageList, senderTwitterName) {
    if(messageList != null && messageList.prevMessages != null) {
        for (var i = 0; i < messageList.prevMessages.length; i++) {
            var message = messageList.prevMessages[i];
            var newMessage = document.createElement("div");
            newMessage.className = "full-width outerMessageBox";

            if(message.sender == senderTwitterName) {
                newMessage.innerHTML = "<span class='message-box-current-user'><p>" + message.message + "</p></span>";
            } else {
                newMessage.innerHTML = "<span class='message-box-other-user'><p>" + message.message + "</p></span>";
            }

            document.getElementById("messageFeed").appendChild(newMessage);
        }
    }
}

/**
 * Handles the lack of more messages
 * @param error
 */
function failedToLoadMoreMessages(error) {
    //Nothing to do
}