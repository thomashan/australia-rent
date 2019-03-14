package thomashan.github.io.australia.rent.domain

import spock.lang.Specification
import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.SearchQuery

import static java.util.Optional.empty

class RentDomainRepositorySpec extends Specification {
    private RentDomainRepository rentDomainRepository = new RentDomainRepository()

    def "get all listing"() {
        given:
        System.setProperty("webdriver.gecko.driver", "/Users/thomashan/.gradle/webdriver/geckodriver/0.23.0/geckodriver-v0.23.0-macos.tar/9r5ttm0hwphar5jcjsfet7sl9/geckodriver")

        when:
        List<RentDetails> rentDetails = rentDomainRepository.findAll(new SearchQuery(550, 600, 3, empty()))

        then:
        rentDetails.size() > 0
        rentDetails.each { rentDetail ->
            println(rentDetail)
        }
        println(rentDetails.size())
        rentDetails.collect { it.suburb }.toSet().sort().each {
            println(it)
        }
    }
}
