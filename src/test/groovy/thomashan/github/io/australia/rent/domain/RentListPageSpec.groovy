package thomashan.github.io.australia.rent.domain

import geb.spock.GebReportingSpec
import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.search.SearchQuery

import static java.util.Optional.empty as e
import static java.util.Optional.of

class RentListPageSpec extends GebReportingSpec {
    private SearchQuery searchQuery = new SearchQuery(e(), of(550), of(600), of(3), of(3))

    def "list only includes non ad listing"() {
        when:
        to RentListPage, searchQuery

        then:
        list.size() == 20
    }

    def "get the correct page number"() {
        when:
        to RentListPage, searchQuery

        then:
        pageNumber == 1
    }

    def "get the correct information"() {
        when:
        to RentListPage, searchQuery

        then:
        RentDetails rentDetails = rentDetails[0]
        rentDetails.price.get() >= 550 && rentDetails.price.get() <= 600
        rentDetails.suburb.startsWith("A")
        rentDetails.state == "VIC"
        rentDetails.postcode.startsWith("3")
        rentDetails.bedrooms == 3
        rentDetails.bathrooms
        rentDetails.parking >= 0
    }

    def "get current page number"() {
        when:
        to RentListPage, searchQuery

        then:
        pageNumber == 1
    }

    def "get end page number"() {
        when:
        to RentListPage, searchQuery

        then:
        pageEnd == 5
    }
}
