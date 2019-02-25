package thomashan.github.io.australia.rent

import geb.spock.GebSpec
import thomashan.github.io.australia.rent.domain.RentListPage

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
        to RentListPage
        def temp = rentDetails

        then:
        rentDetails[0].price.get() == 590
        rentDetails[0].address == "8/19-21 Matthews Avenue"
        rentDetails[0].suburb == "AIRPORT WEST"
        rentDetails[0].state == "VIC"
        rentDetails[0].postcode == "3042"
    }
}
