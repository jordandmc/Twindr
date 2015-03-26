To run the acceptance tests:
    Launch Play using the test.conf file (-Dconfig.file=conf/test.conf or -Dconfig.resource=test.conf [whichever you have setup]). This will allow you access to the TestController functions.
    Open the Selenium IDE
    Load the AcceptanceTestSuite.html file
    Run the tests at no more than 50% speed

Keep in mind that running the tests will reset your local test database
NOTE: If a test fails, you'll need to manually logout of Twitter or delete the Twitter auth_token cookie in Firefox