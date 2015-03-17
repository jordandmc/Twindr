describe("send message", function() {

    beforeEach(function() {
        $(document.body).append($("<div id='main'><div id='messageFeed'></div><div id='sendMessage'><textarea id='messageBox' placeholder='Type a message here'></textarea><a id='sendMessageBtn' onclick='sendMessage(abc, 123)' href='#'>Send</a></div></div>"));
        document.getElementById('messageBox').value = "test message";
    });

    afterEach(function() {
        document.getElementById("main").remove();
    });

    it("should do nothing if there is no matchID", function() {
        sendMessage(null, null);

        expect(document.getElementById('messageBox').value).toBe("test message");
        expect(document.getElementById('messageFeed').children.length).toBe(0);
    });  

    it("should do nothing if there is no matchID", function() {
        sendMessage("", null);

        expect(document.getElementById('messageBox').value).toBe("test message");
        expect(document.getElementById('messageFeed').children.length).toBe(0);
    }); 

    it("should do nothing if there is no sender", function() {
        sendMessage("12345679", null);

        expect(document.getElementById('messageBox').value).toBe("test message");
        expect(document.getElementById('messageFeed').children.length).toBe(0);
    }); 

    it("should do nothing if there is no sender", function() {
        sendMessage("12345679", "");

        expect(document.getElementById('messageBox').value).toBe("test message");
        expect(document.getElementById('messageFeed').children.length).toBe(0);
    }); 

    it("should send a message to the server and update the local message feed", function() {
        spyOn($, "post").andCallFake(function(options) {
            return options.success();
        });

        sendMessage("123456789", "twitterUser");
        expect(document.getElementById('messageBox').value).toBe("");
    }); 

});

describe("adding a message", function() {

    var testMessageObj  = { _id: "", matchID: "123456789", sender: "twitter1", message: "Test message", dateTime: (new Date()).getTime() };
   
    beforeEach(function() {
        $(document.body).append($("<div id='main'><div id='messageFeed'></div><div id='sendMessage'><textarea id='messageBox' placeholder='Type a message here'></textarea><a id='sendMessageBtn' onclick='sendMessage(abc, 123)' href='#'>Send</a></div></div>"));
    });

    afterEach(function() {
        document.getElementById("main").remove();
    });

    it("should not add an empty message", function() {
        addMessageDiv(null, null);

        expect(document.getElementById('messageFeed').children.length).toBe(0);
    });

    it("should add a message with the current user as the sender", function() {
        addMessageDiv(testMessageObj, "twitter1");

        expect(document.getElementById('messageFeed').children.length).toBe(1);
        expect(document.getElementById('messageFeed').children[0].getElementsByTagName("span")[0].className).toBe('message-box-current-user');
    });

    it("should add a message with the other user as the sender", function() {
        addMessageDiv(testMessageObj, "twitter2");

        expect(document.getElementById('messageFeed').children.length).toBe(1)
        expect(document.getElementById('messageFeed').children[0].getElementsByTagName("span")[0].className).toBe('message-box-other-user');
    });

});

describe("loading more messages", function(){

    it("should not load any messages with no matchID", function() {
        spyOn($, "ajax").andCallFake(function(options) {
            return options.success();
        });

        var successCallback = jasmine.createSpy();
        var errorCallback   = jasmine.createSpy();

        loadMoreMessages(null, null, successCallback, errorCallback);
        expect(successCallback).not.toHaveBeenCalled();
        expect(errorCallback).not.toHaveBeenCalled();
    });

    it("should not load any messages with no matchID", function() {
        spyOn($, "ajax").andCallFake(function(options) {
            return options.success();
        });

        var successCallback = jasmine.createSpy();
        var errorCallback   = jasmine.createSpy();

        loadMoreMessages("", null, successCallback, errorCallback);
        expect(successCallback).not.toHaveBeenCalled();
        expect(errorCallback).not.toHaveBeenCalled();
    });

    it("should call the server", function() {
        spyOn($, "ajax").andCallFake(function(options) {
            return options.success();
        });

        var successCallback = jasmine.createSpy();
        var errorCallback   = jasmine.createSpy();

        loadMoreMessages("123456789", "sender", successCallback, errorCallback);
        expect(successCallback).toHaveBeenCalled();
        expect(errorCallback).not.toHaveBeenCalled();
    });

});

describe("adding a list of messages", function() {

    var messagesList = { prevMessages : [ 
                            { _id: "", matchID: "123456789", sender: "twitter1", message: "Test message", dateTime: (new Date()).getTime() },
                            { _id: "", matchID: "123456789", sender: "twitter2", message: "Another message", dateTime: (new Date()).getTime() },
                            { _id: "", matchID: "123456789", sender: "twitter2", message: "Hey! Listen!", dateTime: (new Date()).getTime() } 
                        ] };
   
    beforeEach(function() {
        $(document.body).append($("<div id='main'><div id='messageFeed'></div><div id='sendMessage'><textarea id='messageBox' placeholder='Type a message here'></textarea><a id='sendMessageBtn' onclick='sendMessage(abc, 123)' href='#'>Send</a></div></div>"));
    });

    afterEach(function() {
        document.getElementById("main").remove();
    });

    it('should not add an empty list of messages', function() {
        addMoreMessages(null, null);
        expect(document.getElementById('messageFeed').children.length).toBe(0);    
    });

    it('should add a list of three messages', function() {
        addMoreMessages(messagesList, "twitter1");
        expect(document.getElementById('messageFeed').children.length).toBe(3);
    });

});