package thomashan.github.io.australia.rent

import geb.spock.GebSpec
import thomashan.github.io.australia.rent.domain.RentListPage

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
        rentDetails[0].price.get() >= 550 && rentDetails[0].price.get() <= 600
        rentDetails[0].suburb == "AIRPORT WEST"
        rentDetails[0].state == "VIC"
        rentDetails[0].postcode == "3042"
    }
}
