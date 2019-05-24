# australia-rent

[![Build Status](https://travis-ci.org/thomashan/australia-rent.svg?branch=master)](https://travis-ci.org/thomashan/australia-rent)

## Setting environment variables
Set the following environment variables:
```
DROPBOX_ACCESS_TOKEN
GOOGLE_API_KEY
```

## Running with different browser drivers
The default driver is HtmlUnit which is a headless driver.
If you want to see the driver change `src/main/resources/GebConfig.groovy`
```groovy
driver = { new HtmlUnitDriver() } // default
// driver = { new FirefoxDriver() } // enable for firefox
```

## Running tests in Intellij
We need to set the system property `webdriver.gecko.driver` if we want to run tests in intellij.

Make sure you have something like this in your test
```
System.setProperty("webdriver.gecko.driver", "${user.home}/.gradle/webdriver/geckodriver/${version}/geckodriver-v${version}-${os}.tar/${random}/geckodriver")
```


## Running targeted tests
```bash
> ./gradlew test --tests *RunSpec*
```
