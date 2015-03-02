/**
 *
 * Provides the ability to accept or reject a match request.
 *
 */

/**
 * Accepts the current match and loads the next one.
 * @param twitterName The name of the twitter user we want to match with
 */
function acceptMatch(twitterName) {
    jsRoutes.controllers.MatchingController.acceptMatch(twitterName).ajax({
        success: updateMatchBox,
        error: matchingError
    });
}

/**
 * Rejects the current match and loads the next one/
 * @param twitterName The name of the twitter user we don't want to match with
 */
function rejectMatch(twitterName) {
    jsRoutes.controllers.MatchingController.rejectMatch(twitterName).ajax({
        success: updateMatchBox,
        error: matchingError
    });
}

/**
 * Updates the relevant fields in matchesFeed.scala.html
 * @param potentialMatch JSON object containing the username and recent tweets of a user
 */
function updateMatchBox(potentialMatch) {
    var tweets = "";
    for(var i = 0; i < potentialMatch.tweets.length; i++) {
        tweets += potentialMatch.tweets[i] + "<br/>";
    }

    $('#potential-match-username').text(potentialMatch.username);
    $('#potential-match-tweets').html(tweets);
    $('#accept-match').html("<a href='#' onclick='acceptMatch(\"" + potentialMatch.username + "\")'>Match</a>");
    $('#reject-match').html("<a href='#' onclick='rejectMatch(\"" + potentialMatch.username + "\")'>Decline</a>");
    document.getElementById('has-potential-match').style.visibility = "visible";
    document.getElementById('not-matched').style.visibility = "visible";
}

/**
 * An error has occurred or we have no more matches to suggest
 * @param msg The error message
 */
function matchingError(msg) {
    var message = "";
    if(msg.responseText == "No matches") {
        message = "We have no potential matches to suggest at this time.";
    }
    else {
        message = "An error has occurred. Please try again.";
    }

    $('#potential-match-username').text("");
    $('#potential-match-tweets').html(message);
    $('#has-potential-match').html("");
    document.getElementById('has-potential-match').style.visibility = "hidden";
    document.getElementById('not-matched').style.visibility = "hidden";
}

/**
 * Called when the page is loaded to populate the first potential match
 */
function loadFirstPotentialMatch() {
    jsRoutes.controllers.MatchingController.getFirstMatch().ajax({
        success: updateMatchBox,
        error: matchingError
    });
}