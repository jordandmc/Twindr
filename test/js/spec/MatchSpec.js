describe("Accept match", function() {

    it("should send accept match notification to server", function() {
        spyOn($, "ajax");
        acceptMatch("twitterUser");
        expect($.ajax.mostRecentCall.args[0]["url"]).toEqual("/ajax/acceptMatch?twitterName=twitterUser");
    });

    it("should send accept match notification to server and obtain success response", function() {
        spyOn($, "ajax").andCallFake(function(options) {
           return options.success();
        });

        var acceptCallback = jasmine.createSpy();
        var errorCallback  = jasmine.createSpy();

        acceptMatch("twitterUser", acceptCallback, errorCallback);
        expect(acceptCallback).toHaveBeenCalled();
        expect(errorCallback).not.toHaveBeenCalled();
    });

    it("should handle an invalid response", function() {
        spyOn($, "ajax").andCallFake(function(options) {
            return options.error({responseText: "No matches"});
        });

        var acceptCallback = jasmine.createSpy();
        var errorCallback  = jasmine.createSpy();

        acceptMatch("twitterUser", acceptCallback, errorCallback);
        expect(acceptCallback).not.toHaveBeenCalled();
        expect(errorCallback).toHaveBeenCalledWith({responseText: "No matches"});
    });
});

describe("Reject match", function() {

    it("should send reject match notification to server", function() {
         spyOn($, "ajax");
         rejectMatch("twitterUser");
         expect($.ajax.mostRecentCall.args[0]["url"]).toEqual("/ajax/rejectMatch?twitterName=twitterUser");
     });

    it("should send a reject match notification to server and obtain success response", function() {
        spyOn($, "ajax").andCallFake(function(options) {
            return options.success();
        });

        var acceptCallback = jasmine.createSpy();
        var errorCallback  = jasmine.createSpy();

        rejectMatch("twitterUser", acceptCallback, errorCallback);
        expect(acceptCallback).toHaveBeenCalled();
        expect(errorCallback).not.toHaveBeenCalled();
    });

    it("should handle an invalid response", function() {
        spyOn($, "ajax").andCallFake(function(options) {
            return options.error({responseText: "No matches"});
        });

        var acceptCallback = jasmine.createSpy();
        var errorCallback  = jasmine.createSpy();

        rejectMatch("twitterUser", acceptCallback, errorCallback);
        expect(acceptCallback).not.toHaveBeenCalled();
        expect(errorCallback).toHaveBeenCalledWith({responseText: "No matches"});
    });
});

describe("Unmatch", function() {

    it("should send unmatch notification to server", function() {
         spyOn($, "ajax");
         unmatch("twitterUser");
         expect($.ajax.mostRecentCall.args[0]["url"]).toEqual("/ajax/unmatch?twitterName=twitterUser");
     });

    it("should send an unmatch notification to server and obtain success response", function() {
        spyOn($, "ajax").andCallFake(function(options) {
            return options.success();
        });

        var acceptCallback = jasmine.createSpy();
        var errorCallback  = jasmine.createSpy();

        unmatch("twitterUser", acceptCallback, errorCallback);
        expect(acceptCallback).toHaveBeenCalled();
        expect(errorCallback).not.toHaveBeenCalled();
    });

    it("should handle an invalid response", function() {
        spyOn($, "ajax").andCallFake(function(options) {
            return options.error({responseText: "No matches"});
        });

        var acceptCallback = jasmine.createSpy();
        var errorCallback  = jasmine.createSpy();

        unmatch("twitterUser", acceptCallback, errorCallback);
        expect(acceptCallback).not.toHaveBeenCalled();
        expect(errorCallback).toHaveBeenCalledWith({responseText: "No matches"});
    });
});

describe("removeUserFromPage", function() {

    beforeEach(function() {
        $(document.body).append($("<div><ul id='match-list'><li id='match-user-twitterUser'></li></ul></div>"));
    });

    afterEach(function() {
        document.getElementById("match-list").remove();
    });

    it("should remove an existing user", function() {
        removeUserFromPage("twitterUser");

        expect(document.getElementById('match-list')).not.toBe(null);
        expect(document.getElementById('match-list').children.length).toBe(0);
        expect(document.getElementById("match-user-twitterUser")).toBe(null);
    });

    it("should not remove a non existent user", function() {
        removeUserFromPage("anotherTwitterUser");

        expect(document.getElementById("match-list")).not.toBe(null);
        expect(document.getElementById('match-list').children.length).toBe(1);
        expect(document.getElementById("match-user-anotherTwitterUser")).toBe(null);
    });

    it("should do nothing with an empty list", function() {
        removeUserFromPage("twitterUser");
        removeUserFromPage("twitterUser");

        expect(document.getElementById("match-list")).not.toBe(null);
        expect(document.getElementById('match-list').children.length).toBe(0);
        expect(document.getElementById("match-user-anotherTwitterUser")).toBe(null);
    });
});

describe("updateMatchBox", function() {

    beforeEach(function() {
        //Watered down match feed html (ie. no scala, css or any real formatting)
        $(document.body).append($("<div id='match' class='match-box'><label id='potential-match-username'></label><label id='has-potential-match' style='visibility: hidden'>&nbsp;tweeted:</label><p id='potential-match-tweets'></p><span id='not-matched' class='float-right'><label id='accept-match'></label>&nbsp;<label id='reject-match'></label></span></div>"));
    });

    it("should show no matches", function() {
        potentialMatchList = null;
        currentIndex = 0;
        updateMatchBox();
        expect(document.getElementById("potential-match-tweets").innerHTML).toBe(""); //Needs server for response
    });

    it("should show a single match", function() {
        potentialMatchList = JSON.parse('{ "matches": [ {"username": "twitterUser", "tweets": ["Apples"] } ] }');
        currentIndex = 0;
        updateMatchBox();
        expect(document.getElementById("potential-match-tweets").innerHTML).toContain("Apples");
    });

    it("should show a single match", function() {
        potentialMatchList = JSON.parse('{ "matches": [ {"username": "twitterUser", "tweets": ["Apples"] }, { "username": "twitter2", "tweets": ["Lions"] } ] }');
        currentIndex = 1;
        updateMatchBox();
        expect(document.getElementById("potential-match-tweets").innerHTML).toContain("Lions");
    });
});

describe("matchingError", function() {

    it("should recieve no matches message", function() {
        matchingError(JSON.parse('{ "responseText": "No matches" }'));
        expect(document.getElementById("potential-match-tweets").innerHTML).toContain("We have no potential matches to suggest at this time.");
    });

    it("should recieve an error message", function() {
        matchingError("WHAT IS THIS?");
        expect(document.getElementById("potential-match-tweets").innerHTML).toContain("An error has occurred");
    });

    it("should still recieve an error message", function() {
        matchingError(null);
        expect(document.getElementById("potential-match-tweets").innerHTML).toContain("An error has occurred");
    });
});

describe("updateWithNewMatches", function() {

    it("should recieve an error message with a null match list", function() {
        updateWithNewMatches(null);
        expect(document.getElementById("potential-match-tweets").innerHTML).toContain("no potential matches");
    });

    it("should recieve an error message with an empty list", function() {
        updateWithNewMatches(JSON.parse('{ "matches": [] }'));
        expect(document.getElementById("potential-match-tweets").innerHTML).toContain("no potential matches");
    });

    it("should see the first tweet", function() {
        updateWithNewMatches(JSON.parse('{ "matches": [ {"username": "twitterUser", "tweets": ["Apples"] } ] }'));
        expect(document.getElementById("potential-match-tweets").innerHTML).toContain("Apples");
    });
});

describe("loadPotentialMatches", function() {

    it("should recieve an error message from server", function() {
        spyOn($, "ajax").andCallFake(function(options) {
            return options.error();
        });

        var acceptCallback = jasmine.createSpy();
        var errorCallback  = jasmine.createSpy();

        loadPotentialMatches(acceptCallback, errorCallback);
        expect(acceptCallback).not.toHaveBeenCalled();
        expect(errorCallback).toHaveBeenCalled();
    });

    it("should recieve a success message from server", function() {
        spyOn($, "ajax").andCallFake(function(options) {
            return options.success();
        });

        var acceptCallback = jasmine.createSpy();
        var errorCallback  = jasmine.createSpy();

        loadPotentialMatches(acceptCallback, errorCallback);
        expect(acceptCallback).toHaveBeenCalled();
        expect(errorCallback).not.toHaveBeenCalled();
    });

    it("should show new matches", function() {
        spyOn($, "ajax").andCallFake(function(options) {
            return options.success(JSON.parse('{ "matches": [ {"username": "twitterUser", "tweets": ["Apples"] }, { "username": "twitter2", "tweets": ["Lions"] } ] }'));
        });

        var acceptCallback = jasmine.createSpy();
        var errorCallback  = jasmine.createSpy();

        loadPotentialMatches(acceptCallback, errorCallback);
        expect(acceptCallback).toHaveBeenCalled();
        expect(errorCallback).not.toHaveBeenCalled();

        expect(document.getElementById("potential-match-tweets").innerHTML).toContain("Apples");
    });
});