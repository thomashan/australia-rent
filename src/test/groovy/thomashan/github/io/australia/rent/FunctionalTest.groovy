package thomashan.github.io.australia.rent

import geb.Browser
import org.junit.jupiter.api.Test
import org.openqa.selenium.firefox.FirefoxDriver

class FunctionalTest {
    @Test
    void "test"() {
        System.setProperty("webdriver.gecko.driver", "download/geckodriver")
//        println(System.properties.get("webdriver.gecko.driver"))
//        def browser = new Browser(driver: new FirefoxDriver())

        Browser.drive{
            to RentDetailsPage
            assert price == "\$600 per week"
        }
    }
}
