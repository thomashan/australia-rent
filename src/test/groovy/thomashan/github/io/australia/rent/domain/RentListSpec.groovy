package thomashan.github.io.australia.rent.domain

import geb.spock.GebReportingSpec
import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.SearchQuery

import static java.util.Optional.empty

class RentListSpec extends GebReportingSpec {
    def "get the correct information"() {
        when:
        to RentListPage, new SearchQuery(550, 600, 3, empty())

        then:
        list.size() == 20
        pageNumber == 1
        RentDetails rentDetails = rentDetails[0]
        rentDetails.price.get() >= 550 && rentDetails.price.get() <= 600
        rentDetails.suburb.startsWith("A")
        rentDetails.state == "VIC"
        rentDetails.postcode.startsWith("3")
    }
}
