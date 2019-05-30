package thomashan.github.io.australia.rent.domain


import spock.lang.Specification
import thomashan.github.io.australia.rent.SearchQuery

import static java.util.Optional.empty as e
import static java.util.Optional.of

class RentListPageUnitSpec extends Specification {
    def "suburbs no suburb"() {
        when:
        String region = RentListPage.suburbs(new SearchQuery(e(), e(), e(), e(), e()))

        then:
        region == "melbourne-region-vic"
    }

    def "suburbs empty suburb"() {
        when:
        String region = RentListPage.suburbs(new SearchQuery(of([]), e(), e(), e(), e()))

        then:
        region == "melbourne-region-vic"
    }

    def "suburbs single suburb"() {
        when:
        String region = RentListPage.suburbs(new SearchQuery(of(["parkville-vic-3052"]), e(), e(), e(), e()))

        then:
        region == "parkville-vic-3052"
    }

    def "region multiple suburbs"() {
        when:
        String region = RentListPage.suburbs(new SearchQuery(of(["parkville-vic-3052", "carlton-vic-3053"]), e(), e(), e(), e()))

        then:
        region == "suburb=parkville-vic-3052,carlton-vic-3053"
    }

    def "bedrooms empty minBedroom and empty maxBedroom"() {
        when:
        String bedrooms = RentListPage.bedrooms(new SearchQuery(e(), e(), e(), e(), e()))

        then:
        bedrooms == ""
    }

    def "bedrooms minBedroom defined and empty maxBedroom"() {
        when:
        String bedrooms = RentListPage.bedrooms(new SearchQuery(e(), e(), e(), of(1), e()))

        then:
        bedrooms == "&bedrooms=1-any"
    }

    def "bedrooms minBedroom defined and maxBedroom defined"() {
        when:
        String bedrooms = RentListPage.bedrooms(new SearchQuery(e(), e(), e(), of(1), of(1)))

        then:
        bedrooms == "&bedrooms=1-1"
    }

    def "bedrooms empty minBedroom and maxBedroom defined"() {
        when:
        String bedrooms = RentListPage.bedrooms(new SearchQuery(e(), e(), e(), e(), of(1)))

        then:
        bedrooms == "&bedrooms=0-1"
    }


    def "price empty minPrice and empty maxPrice"() {
        when:
        String price = RentListPage.price(new SearchQuery(e(), e(), e(), e(), e()))

        then:
        price == ""
    }

    def "price minPrice defined and empty maxPrice"() {
        when:
        String price = RentListPage.price(new SearchQuery(e(), of(100), e(), e(), e()))

        then:
        price == "&price=100-any"
    }

    def "price minPrice defined and maxPrice defined"() {
        when:
        String price = RentListPage.price(new SearchQuery(e(), of(100), of(200), e(), e()))

        then:
        price == "&price=100-200"
    }

    def "price empty minPrice and maxPrice defined"() {
        when:
        String price = RentListPage.price(new SearchQuery(e(), e(), of(200), e(), e()))

        then:
        price == "&price=0-200"
    }
}
