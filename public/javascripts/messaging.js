/**
 * Sends the message contained within the message box (provided there is one) from the
 * given sender to the given receiver.
 * @param matchID The unique identifier for the match. Used to send messages to specific a server sent event handler
 * @param sender The twitter name of the sender
 */
function sendMessage(matchID, sender) {
    var message = document.getElementById("messageBox").value;

    if(message != null && message != "") {
        var msg = { matchID: matchID, sender: sender, message: message, time: (new Date()).toString() };

        jsRoutes.controllers.MessagingController.sendMessage().ajax({
            data: JSON.stringify(msg),
            dataType: 'json',
            contentType: 'application/json',
            type: 'post'
        });

        document.getElementById("messageBox").value = "";
    }
}