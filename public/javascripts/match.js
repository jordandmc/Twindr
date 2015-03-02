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
        contentType: 'application/json',
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
        contentType: 'application/json',
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
        tweets += potentialMatch.tweets[i] + "\n";
    }

    $('#potential-match-username').text(potentialMatch.username);
    $('#potential-match-tweets').text(tweets);
}

/**
 * An error has occurred or we have no more matches to suggest
 * @param msg The error message
 */
function matchingError(msg) {
    if(msg == "No matches") {
        //Do something
    }
}

/**
 * Called when the page is loaded to populate the first potential match
 */
function loadFirstPotentialMatch() {
    jsRoutes.controllers.MatchingController.getFirstMatch().ajax({
        contentType: 'application/json',
        success: updateMatchBox,
        error: matchingError
    });
}