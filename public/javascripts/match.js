/**
 *
 * Provides the ability to accept or reject a match request.
 *
 */

/**
 * Local cache of potential matches. Does not persist from page to page. Once this
 * list is depleted, make a call to getPotentialMatches() to try and obtain more.
 */

var potentialMatchList = null;
var currentIndex = 0;

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
 * Unmatches with a previously matched user
 * @param twitterName The name of the twitter user we want to forget
 */
function unmatch(twitterName) {
    jsRoutes.controllers.MatchingController.unmatch(twitterName).ajax({
        success: function() { removeUserFromPage(twitterName); },
        error: unmatchError
    });
}

/**
 * Removes the given twitter username from the page
 * @param twitterName The twitter name of the user to remove
 */
function removeUserFromPage(twitterName) {
    var element = document.getElementById("match-user-" + twitterName);
    element.parentNode.removeChild(element);

    var list = document.getElementById('match-list');
    if(list.children.length <= 0) {
        $('#match').html("<p>We have no matches for you at this time.</p>");
    }
}

/**
 * An error has occurred removing a user
 * @param msg The error message
 */
function unmatchError(msg) {

}

/**
 * Updates the relevant fields in matchesFeed.scala.html
 */
function updateMatchBox() {
    if(currentIndex < potentialMatchList.matches.length) {
        var potentialMatch = potentialMatchList.matches[currentIndex];
        var tweets = "";
        for (var i = 0; i < potentialMatch.tweets.length; i++) {
            tweets += potentialMatch.tweets[i] + "<br/>";
        }

        $('#potential-match-username').text(potentialMatch.username);
        $('#potential-match-tweets').html(tweets);
        $('#accept-match').html("<a class='main-button text-like-link' href='#' onclick='acceptMatch(\"" + potentialMatch.username + "\")'>Match</a>");
        $('#reject-match').html("<a class='main-button text-like-link' href='#' onclick='rejectMatch(\"" + potentialMatch.username + "\")'>Decline</a>");
        document.getElementById('has-potential-match').style.visibility = "visible";
        document.getElementById('not-matched').style.visibility = "visible";

        currentIndex++;
    }
    else {
        //There are no more matches in the list. We need to try and load more.
        loadPotentialMatches();
    }
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
 * Success callback for updating the global match list. We update our cached list to
 * see the new potential matches and reset the index into that list. In the event
 * (this should never happen as loadPotentialMatches automatically calls matchingError
 * if the list is empty) where the potential match list is empty, we will treat this
 * as a 'No matches' error.
 *
 * @param matchList The new list of matches
 */
function updateWithNewMatches(matchList) {
    potentialMatchList = matchList;
    currentIndex = 0;

    if(potentialMatchList.matches.length > 0) {
        updateMatchBox();
    }
    else {
        matchingError("No matches");
    }
}

/**
 * Called when the page is loaded and on subsequent calls to get more matches to populate
 * the potential match list. If there are no potential matches, we will automatically
 * call the error callback.
 */
function loadPotentialMatches() {
    jsRoutes.controllers.MatchingController.getPotentialMatches().ajax({
        success: updateWithNewMatches,
        error: matchingError
    });
}