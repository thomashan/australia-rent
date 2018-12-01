# australia-rent

[![Build Status](https://travis-ci.org/thomashan/australia-rent.svg?branch=master)](https://travis-ci.org/thomashan/australia-rent)

## Running tests in Intellij
We need to set the system property `webdriver.gecko.driver` if we want to run tests in intellij.

Make sure you have something like this in your test
```
System.setProperty("webdriver.gecko.driver", "${user.home}/.gradle/webdriver/geckodriver/${version}/geckodriver-v${version}-${os}.tar/${random}/geckodriver")
```
