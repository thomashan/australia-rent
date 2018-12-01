package thomashan.github.io.australia.rent

import geb.Browser
import org.junit.jupiter.api.Test
import thomashan.github.io.australia.rent.domain.RentDetailsPage
import thomashan.github.io.australia.rent.domain.RentListPage


class FunctionalTest {
    @Test
    void "test"() {
        System.setProperty("webdriver.gecko.driver", "/Users/thomashan/.gradle/webdriver/geckodriver/0.23.0/geckodriver-v0.23.0-macos.tar/9r5ttm0hwphar5jcjsfet7sl9/geckodriver")
        println(System.properties.get("webdriver.gecko.driver"))

        Browser.drive {
            to RentListPage
            println(list.size())
            assert list.size() == 20
        }
    }
}
