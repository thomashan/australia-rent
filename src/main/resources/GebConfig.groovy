import geb.report.CompositeReporter
import geb.report.PageSourceReporter
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import thomashan.github.io.australia.rent.geb.FullPageScreenshotReporter

//import org.openqa.selenium.htmlunit.HtmlUnitDriver

//driver = {
//    FirefoxOptions firefoxOptions = new FirefoxOptions()
//    firefoxOptions.setHeadless(true)
//
//    return new FirefoxDriver(firefoxOptions)
//}
driver = {
    ChromeOptions chromeOptions = new ChromeOptions()
    chromeOptions.setHeadless(true)

    return new ChromeDriver(chromeOptions)
}

reportsDir = "build/geb-reports"
reporter = new CompositeReporter(new FullPageScreenshotReporter(), new PageSourceReporter())
reportOnTestFailureOnly = true
