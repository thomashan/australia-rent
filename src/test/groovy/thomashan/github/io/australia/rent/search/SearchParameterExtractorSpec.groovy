package thomashan.github.io.australia.rent.search

import spock.lang.Specification
import thomashan.github.io.australia.rent.search.SearchParameterExtractor as s

import static java.util.Optional.empty as e
import static java.util.Optional.of

class SearchParameterExtractorSpec extends Specification {
    def "suburbs no suburb"() {
        when:
        String region = s.suburbs(new SearchQuery(e(), e(), e(), e(), e()))

        then:
        region == "melbourne-region-vic"
    }

    def "suburbs empty suburb"() {
        when:
        String region = s.suburbs(new SearchQuery(of([]), e(), e(), e(), e()))

        then:
        region == "melbourne-region-vic"
    }

    def "suburbs single suburb"() {
        when:
        String region = s.suburbs(new SearchQuery(of(["parkville-vic-3052"]), e(), e(), e(), e()))

        then:
        region == "parkville-vic-3052"
    }

    def "region multiple suburbs"() {
        when:
        String region = s.suburbs(new SearchQuery(of(["parkville-vic-3052", "carlton-vic-3053"]), e(), e(), e(), e()))

        then:
        region == "parkville-vic-3052,carlton-vic-3053"
    }

    def "bedrooms empty minBedroom and empty maxBedroom"() {
        when:
        String bedrooms = s.bedrooms(new SearchQuery(e(), e(), e(), e(), e()))

        then:
        bedrooms == ""
    }

    def "bedrooms minBedroom defined and empty maxBedroom"() {
        when:
        String bedrooms = s.bedrooms(new SearchQuery(e(), e(), e(), of(1), e()))

        then:
        bedrooms == "1-any"
    }

    def "bedrooms minBedroom defined and maxBedroom defined"() {
        when:
        String bedrooms = s.bedrooms(new SearchQuery(e(), e(), e(), of(1), of(1)))

        then:
        bedrooms == "1-1"
    }

    def "bedrooms empty minBedroom and maxBedroom defined"() {
        when:
        String bedrooms = s.bedrooms(new SearchQuery(e(), e(), e(), e(), of(1)))

        then:
        bedrooms == "0-1"
    }


    def "prices empty minPrice and empty maxPrice"() {
        when:
        String price = s.prices(new SearchQuery(e(), e(), e(), e(), e()))

        then:
        price == ""
    }

    def "prices minPrice defined and empty maxPrice"() {
        when:
        String price = s.prices(new SearchQuery(e(), of(100), e(), e(), e()))

        then:
        price == "100+"
    }

    def "prices minPrice defined and maxPrice defined"() {
        when:
        String price = s.prices(new SearchQuery(e(), of(100), of(200), e(), e()))

        then:
        price == "100-200"
    }

    def "prices empty minPrice and maxPrice defined"() {
        when:
        String price = s.prices(new SearchQuery(e(), e(), of(200), e(), e()))

        then:
        price == "0-200"
    }
}
