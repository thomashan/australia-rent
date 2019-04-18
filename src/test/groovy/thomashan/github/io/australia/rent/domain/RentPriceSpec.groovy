package thomashan.github.io.australia.rent.domain

import geb.spock.GebReportingSpec
import thomashan.github.io.australia.rent.SearchQuery

import static java.util.Optional.empty

class RentPriceSpec extends GebReportingSpec {
    def "get the domain.com.au rent price"() {
        when:
        to RentListPage, new SearchQuery(550, 600, 3, empty())

        then:
        rentDetails[0].price.get() >= 550 && rentDetails[0].price.get() <= 600
    }
}
