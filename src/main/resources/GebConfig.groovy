import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
//import org.openqa.selenium.htmlunit.HtmlUnitDriver
//import org.openqa.selenium.firefox.FirefoxDriver

//driver = { new FirefoxDriver() }
//driver = { new HtmlUnitDriver() }
driver = {
    ChromeOptions chromeOptions = new ChromeOptions()
    chromeOptions.addArguments("headless")

    return new ChromeDriver(chromeOptions)
}

reportOnTestFailureOnly = true