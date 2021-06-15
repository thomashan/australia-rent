package thomashan.github.io.australia.rent.search

import spock.lang.Specification

import static java.util.Optional.empty as e
import static java.util.Optional.of

class SearchQuerySpec extends Specification {
    def "getPrices should prepend prices_ if minPrice or maxPrice not empty"() {
        when:
        SearchQuery searchQuery = new SearchQuery(e(), of(200), of(400), e(), e())

        then:
        searchQuery.prices == "prices_200-400"
    }

    def "getPrices should not prepend price_ if minPrice and maxPrice is empty"() {
        when:
        SearchQuery searchQuery = new SearchQuery(e(), e(), e(), e(), e())

        then:
        searchQuery.prices == ""
    }

    def "getBedrooms should prepend bedrooms_ if minBedrooms or maxBedroom not empty"() {
        when:
        SearchQuery searchQuery = new SearchQuery(e(), e(), e(), of(1), of(1))

        then:
        searchQuery.bedrooms == "bedrooms_1-1"
    }

    def "getBedrooms should not prepend bedrooms_ if minBedrooms and maxBedroom is empty"() {
        when:
        SearchQuery searchQuery = new SearchQuery(e(), e(), e(), e(), e())

        then:
        searchQuery.bedrooms == ""
    }

    def "getName prices and bedrooms defined"() {
        when:
        SearchQuery searchQuery = new SearchQuery(e(), of(100), of(200), of(1), of(1))

        then:
        searchQuery.name == "prices_100-200_bedrooms_1-1"
    }

    def "getName prices defined and bedrooms undefined"() {
        when:
        SearchQuery searchQuery = new SearchQuery(e(), of(100), of(200), e(), e())

        then:
        searchQuery.name == "prices_100-200"
    }

    def "getName prices undefined and bedrooms defined"() {
        when:
        SearchQuery searchQuery = new SearchQuery(e(), e(), e(), of(1), of(1))

        then:
        searchQuery.name == "bedrooms_1-1"
    }

    def "getName prices undefined and bedrooms undefined"() {
        when:
        SearchQuery searchQuery = new SearchQuery(e(), e(), e(), e(), e())

        then:
        searchQuery.name == ""
    }
}
