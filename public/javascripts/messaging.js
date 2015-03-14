/**
 * Sends the message contained within the message box (provided there is one) from the
 * given sender to the given receiver.
 * @param sender The twitter name of the sender
 * @param recipient The twitter name of the receiver
 */
function sendMessage(sender, recipient) {
    var message = document.getElementById("messageBox").value;

    if(message != null && message != "") {
        var msg = {sender: sender, recipient: recipient, message: message, time: (new Date()).toString()};

        jsRoutes.controllers.MessagingController.sendMessage().ajax({
            data: JSON.stringify(msg),
            dataType: 'json',
            contentType: 'application/json',
            type: 'post'
        });

        document.getElementById("messageBox").value = "";
    }
}