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
 * @param callback Override for AJAX success callback
 * @param errorCallback Override for AJAX error callback
 */
function acceptMatch(twitterName, callback, errorCallback) {
    jsRoutes.controllers.MatchingController.acceptMatch(twitterName).ajax({
        success: callback || updateMatchBox,
        error: errorCallback || matchingError
    });
}

/**
 * Rejects the current match and loads the next one/
 * @param twitterName The name of the twitter user we don't want to match with
 * @param callback Override for AJAX success callback
 * @param errorCallback Override for AJAX error callback
 */
function rejectMatch(twitterName, callback, errorCallback) {
    jsRoutes.controllers.MatchingController.rejectMatch(twitterName).ajax({
        success: callback || updateMatchBox,
        error: errorCallback || matchingError
    });
}

/**
 * Unmatches with a previously matched user
 * @param twitterName The name of the twitter user we want to forget
 * @param callback Override for AJAX success callback
 * @param errorCallback Override for AJAX error callback
 */
function unmatch(twitterName, callback, errorCallback) {
    jsRoutes.controllers.MatchingController.unmatch(twitterName).ajax({
        success: callback || function() { removeUserFromPage(twitterName); },
        error: errorCallback || unmatchError
    });
}

/**
 * Removes the given twitter username from the page
 * @param twitterName The twitter name of the user to remove
 */
function removeUserFromPage(twitterName) {
    var element = document.getElementById("match-user-" + twitterName);
    if(element)
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
    if(potentialMatchList != null && currentIndex < potentialMatchList.matches.length) {
        var potentialMatch = potentialMatchList.matches[currentIndex];
        var tweets = "";
        if(potentialMatch.tweets.length > 0) {
            for (var i = 0; i < potentialMatch.tweets.length; i++) {
                tweets += potentialMatch.tweets[i] + "<br/>";
            }
        } else {
            tweets = potentialMatch.username + " has not tweeted anything";
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
    if(msg != null && msg.responseText == "No matches") {
        message = "We have no potential matches to suggest at this time.";
    }
    else {
        message = "An error has occurred. Please try again.";
    }

    $('#potential-match-username').text("");
    $('#potential-match-tweets').html(message);
    $('#has-potential-match').html("");

    var hpe = document.getElementById('has-potential-match');
    if(hpe) hpe.style.visibility = "hidden";

    var nm = document.getElementById('not-matched');
    if(nm) nm.style.visibility = "hidden";
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

    if(matchList != null && potentialMatchList.matches.length > 0) {
        updateMatchBox();
    }
    else {
        matchingError(JSON.parse('{ "responseText": "No matches" }'));
    }
}

/**
 * Called when the page is loaded and on subsequent calls to get more matches to populate
 * the potential match list. If there are no potential matches, we will automatically
 * call the error callback.
 * @param callback Override for AJAX success callback
 * @param errorCallback Override for AJAX error callback
 */
function loadPotentialMatches(callback, errorCallback) {
    jsRoutes.controllers.MatchingController.getPotentialMatches().ajax({
        success: callback || updateWithNewMatches,
        error: errorCallback || matchingError
    });
}