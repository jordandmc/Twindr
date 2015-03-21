var target = UIATarget.localTarget();

target.frontMostApp().mainWindow().buttons()["Log in with Twitter"].tap();

// Check if it's the twitter login screen (first-time user)

if(!target.frontMostApp().mainWindow().buttons()["Settings"].checkIsValid()){
    target.frontMostApp().mainWindow().scrollViews()[0].webViews()[0].tapWithOptions({tapOffset:{x:0.20, y:0.26}});
    UIATarget.localTarget().delay(1);
    target.frontMostApp().keyboard().typeString("twindrtest");
    target.frontMostApp().mainWindow().scrollViews()[0].webViews()[0].secureTextFields()["Password"].tap();
    UIATarget.localTarget().delay(1);
    target.frontMostApp().keyboard().typeString("WelcomeToTwitter");
    target.frontMostApp().mainWindow().scrollViews()[0].webViews()[0].buttons()["Sign In"].tap();
}

// Navigate to settings and set it
target.frontMostApp().mainWindow().buttons()["Settings"].tap();
target.frontMostApp().mainWindow().textFields()[0].textFields()[0].tap();
UIATarget.localTarget().delay(1);
target.frontMostApp().windows()[1].pickers()[0].wheels()[0].tapWithOptions({tapOffset:{x:0.52, y:0.61}});
UIATarget.localTarget().delay(1);
target.frontMostApp().windows()[1].pickers()[0].wheels()[0].tapWithOptions({tapOffset:{x:0.52, y:0.37}});
UIATarget.localTarget().delay(1);
target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();
UIATarget.localTarget().delay(1);
target.frontMostApp().mainWindow().textFields()[1].textFields()[0].tap();
UIATarget.localTarget().delay(1);
target.frontMostApp().windows()[1].pickers()[0].wheels()[2].tapWithOptions({tapOffset:{x:0.46, y:0.46}});
UIATarget.localTarget().delay(1);
target.frontMostApp().windows()[1].pickers()[0].wheels()[2].tapWithOptions({tapOffset:{x:0.46, y:0.34}});
UIATarget.localTarget().delay(1);
target.frontMostApp().windows()[1].pickers()[0].wheels()[2].tapWithOptions({tapOffset:{x:0.46, y:0.34}});
UIATarget.localTarget().delay(1);
target.frontMostApp().windows()[1].pickers()[0].wheels()[2].tapWithOptions({tapOffset:{x:0.46, y:0.34}});
UIATarget.localTarget().delay(1);
target.frontMostApp().windows()[1].pickers()[0].wheels()[2].tapWithOptions({tapOffset:{x:0.46, y:0.34}});
UIATarget.localTarget().delay(1);
target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();
target.frontMostApp().mainWindow().textFields()[2].textFields()[0].tap();
UIATarget.localTarget().delay(1);
target.frontMostApp().mainWindow().buttons()["Save"].tap();
target.frontMostApp().navigationBar().leftButton().tap();
target.frontMostApp().mainWindow().buttons()["Logout"].tap();

// Basically there to handle the Geolocation alert
UIATarget.onAlert = function onAlert(alert) {
    var title = alert.name();
    UIALogger.logWarning("Alert with title '" + title + "' encountered.");
    return true;
}
