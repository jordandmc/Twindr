describe("geolocation", function() {

    var geoData;

    beforeEach(function() {
        geoData = JSON.parse('{ "coords": { "latitude": "41.92823", "longitude": "44.123131" } }');
    });

    afterEach(function() {
        geoData = null;
        $.cookie('geolocation-status', null);
    });

    it("should send an update geolocation notification to server", function() {
        spyOn($, "ajax");
        geoPosition(geoData);
        expect($.ajax.mostRecentCall.args[0]["url"]).toEqual("/ajax/updateGeolocation?latitude=41.92823&longitude=44.123131");
    });

    it("should not send an update to the server because the user denied their location", function() {
        spyOn($, "ajax");
        $.cookie('geolocation-status', 'denied');
        getGeolocation();
        expect($.ajax.mostRecentCall.length).toBe(undefined);
    });
});
