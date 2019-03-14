package thomashan.github.io.australia.rent.domain

import geb.spock.GebSpec
import thomashan.github.io.australia.rent.SearchQuery

import static java.util.Optional.empty

class RentListPageSpec extends GebSpec {
    private SearchQuery searchQuery = new SearchQuery(550, 600, 3, empty())

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
        to RentListPage, searchQuery

        then:
        rentDetails[0].price.get() >= 550 && rentDetails[0].price.get() <= 600
        rentDetails[0].suburb == "AIRPORT WEST"
        rentDetails[0].state == "VIC"
        rentDetails[0].postcode == "3042"
    }

    def "get current page number"() {
        when:
        to RentListPage

        then:
        pageNumber == 1
    }

    def "get end page number"() {
        when:
        to RentListPage, searchQuery

        then:
        pageEnd >= 26
    }

    def "getMaxBedrooms should return correct any max bedroom value"() {
        when:
        def maxBedrooms = new RentListPage().getMaxBedrooms(new SearchQuery(1, 1, 1, Optional.empty()))

        then:
        maxBedrooms == "any"
    }

    def "getMaxBedrooms should return correct max bedroom value"() {
        when:
        def maxBedrooms = new RentListPage().getMaxBedrooms(new SearchQuery(1, 1, 1, Optional.of(1)))

        then:
        maxBedrooms == "1"
    }
}
