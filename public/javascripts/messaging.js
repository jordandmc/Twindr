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
function loadMoreMessages(matchID) {
    if(matchID != null && matchID != "") {
        jsRoutes.controllers.MessagingController.getMoreMessages(matchID).ajax({
            type: 'post'
        });
    }
}