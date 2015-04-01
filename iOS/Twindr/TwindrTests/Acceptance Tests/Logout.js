var target = UIATarget.localTarget();
UIATarget.localTarget().delay(1);
target.frontMostApp().mainWindow().buttons()["Log in with Twitter"].tap();
UIATarget.localTarget().delay(1);

// Check if it's the twitter login screen (first-time user on this device)

if(!target.frontMostApp().mainWindow().buttons()["Settings"].checkIsValid()){
    target.frontMostApp().mainWindow().scrollViews()[0].webViews()[0].staticTexts()["Username or email"].tapWithOptions({tapOffset:{x:0.59, y:0.31}});
    UIATarget.localTarget().delay(1);
    target.frontMostApp().keyboard().typeString("twindrtest");
    UIATarget.localTarget().delay(1);
    target.frontMostApp().mainWindow().scrollViews()[0].webViews()[0].secureTextFields()["Password"].tap();
    UIATarget.localTarget().delay(1);
    target.frontMostApp().keyboard().typeString("WelcomeToTwitter");
    UIATarget.localTarget().delay(1);
    target.frontMostApp().keyboard().tapWithOptions({tapOffset:{x:0.16, y:0.92}});
    UIATarget.localTarget().delay(1);
    target.frontMostApp().mainWindow().scrollViews()[0].webViews()[0].buttons()["Sign In"].tap();
    UIATarget.localTarget().delay(1);

    // Check if first time user in general
    if(!target.frontMostApp().mainWindow().buttons()["Settings"].checkIsValid()){
        target.frontMostApp().mainWindow().textFields()[0].textFields()[0].tap();
        UIATarget.localTarget().delay(1);
        target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();
        UIATarget.localTarget().delay(1);
        target.frontMostApp().mainWindow().textFields()[1].textFields()[0].tap();
        UIATarget.localTarget().delay(1);
        target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();
        UIATarget.localTarget().delay(1);
        target.frontMostApp().mainWindow().textViews()[0].tapWithOptions({tapOffset:{x:0.33, y:0.33}});
        UIATarget.localTarget().delay(1);
        target.frontMostApp().keyboard().typeString("Computers");
        UIATarget.localTarget().delay(1);
        target.frontMostApp().mainWindow().buttons()["Save"].tap();
        UIATarget.localTarget().delay(1);
        //target.frontMostApp().alert().buttons()["Allow"].tap();
    }
}

target.frontMostApp().mainWindow().buttons()["Logout"].tap();
UIATarget.localTarget().delay(1);
target.frontMostApp().mainWindow().buttons()["Log in with Twitter"].tap();
UIATarget.localTarget().delay(1);
target.frontMostApp().mainWindow().buttons()["Logout"].tap();

// Basically there to handle the Geolocation alert
UIATarget.onAlert = function onAlert(alert) {
    var title = alert.name();
    UIALogger.logWarning("Alert with title '" + title + "' encountered.");
    return true;
}
