COMP 4350 Group 1 Project "Twindr"
A Tinder clone that uses tweets instead of Facebook pictures.

Comp 4350 Group 1

Tim Sands
Junhyeok Kim
Jordan Cole
Evan Spearman
Morgan Epp
Brett Small
*Git repository: https://github.com/jordandmc/Twindr
Trello: https://trello.com/comp4350group11

Our project is a matchmaking service which uses content from users posts on Twitter (tweets) to match them with other users in their area, unlike other matchmaking services which focus on photographs or user written profiles. For each user, the service presents a queue of other users selected as 'potential matches' for them based on their specified filters (age range, sex, etc.), their most recent geographical location, and common keywords in their tweets. The user is able to see several of the most recent tweets made by a user in their queue of potential matches and decide whether they want to match with them or not. If two users both elect to match with one another, they are recorded as matches. Matched users are able to send messages to one another and should a user decide they no longer wish to communicate with a match, they can elect to 'un-match' that user.

We will be using Twitter to authenticate users and retrieve user's tweets for use with matchmaking. We will use a server-side database to keep track of user data such as authentication tokens, filter preferences and profile data, most recent geographical location, potential matches already suggested, current and previous matches, and messages sent between users. The server will be responsible for generating potential matches between users (based on the previously stated criteria), the logic required to match and un-match users, and that of sending messages between users. The web and iOS clients will be used to relay user input to the server, and display data for the user.
