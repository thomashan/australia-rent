package thomashan.github.io.australia.rent.domain

import geb.spock.GebSpec
import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.SearchQuery

import static java.util.Optional.empty

class RentListSpec extends GebSpec {
    def "list only includes non ad listing"() {
        when:
        to RentListPage

        then:
        list.size() == 20
    }

    def "get the correct page number"() {
        when:
        to RentListPage

        then:
        pageNumber == 1
    }

    def "get the correct information"() {
        when:
        to RentListPage, new SearchQuery(550, 600, 3, empty())
        def temp = rentDetails

        then:
        RentDetails rentDetails = rentDetails[0]
        rentDetails.price.get() >= 550 && rentDetails.price.get() <= 600
        rentDetails.suburb.startsWith("A")
        rentDetails.state == "VIC"
        rentDetails.postcode.startsWith("3")
    }
}
