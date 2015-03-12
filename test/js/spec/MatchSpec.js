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

        var callback = jasmine.createSpy();
        acceptMatch("twitterUser", callback);
        expect(callback).toHaveBeenCalled();
    });
});

describe("Reject match", function() {

    it("should send reject match notification to server", function() {
         spyOn($, "ajax");
         rejectMatch("twitterUser");
         expect($.ajax.mostRecentCall.args[0]["url"]).toEqual("/ajax/rejectMatch?twitterName=twitterUser");
     });

    it("should receive a successful response from the server", function() {
        spyOn($, "ajax").andCallFake(function(options) {
            return options.success();
        });

        var callback = jasmine.createSpy();
        rejectMatch("harold", callback);
        expect(callback).toHaveBeenCalled();
    });
});

describe("Unmatch", function() {

    it("should send unmatch notification to server", function() {
         spyOn($, "ajax");
         unmatch("twitterUser");
         expect($.ajax.mostRecentCall.args[0]["url"]).toEqual("/ajax/unmatch?twitterName=twitterUser");
     });

    it("should send unmatch notification to server and obtain success response", function() {
        spyOn($, "ajax").andCallFake(function(options) {
            return options.success();
        });

        var callback = jasmine.createSpy();
        unmatch("harold", callback);
        expect(callback).toHaveBeenCalled();
    });
});