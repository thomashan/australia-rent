package thomashan.github.io.australia.rent.domain

import geb.spock.GebReportingSpec
import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.search.SearchQuery

import static java.util.Optional.empty as e
import static java.util.Optional.of

class RentDomainRepositorySpec extends GebReportingSpec {
    private RentDomainRepository rentDomainRepository = new RentDomainRepository()
    private RentDetails rentDetailsWithPrice = new RentDetails(of(200), "anonAddress", "anonSuburb", "anonState", "anonPostcode", 0, 0, 0, e())
    private RentDetails rentDetailsWithoutPrice = new RentDetails(e(), "anonAddress", "anonSuburb", "anonState", "anonPostcode", 0, 0, 0, e())

    def "get all listing"() {
        when:
        List<RentDetails> rentDetails = rentDomainRepository.findAll(new SearchQuery(e(), of(4500), of(5000), of(3), e()))

        then:
        rentDetails.size() > 0
        println(rentDetails.size())
        rentDetails.collect { it.suburb }.toSet().sort().each {
            println(it)
        }
    }

    def "filterResultsWithSearchQuery should include rent details without price"() {
        given:
        SearchQuery searchQuery = new SearchQuery(e(), of(4500), of(5000), of(3), e())

        when:
        List<RentDetails> rentDetails = RentDomainRepository.filterResultsWithSearchQuery([rentDetailsWithoutPrice], searchQuery)

        then:
        !rentDetails.empty
    }

    def "filterResultsWithSearchQuery should include rent details price and no search query minPrice"() {
        given:
        SearchQuery searchQuery = new SearchQuery(e(), e(), of(5000), of(3), e())

        when:
        List<RentDetails> rentDetails = RentDomainRepository.filterResultsWithSearchQuery([rentDetailsWithPrice], searchQuery)

        then:
        !rentDetails.empty

    }

    def "filterResultsWithSearchQuery should include rent details price and no search query maxPrice"() {
        given:
        SearchQuery searchQuery = new SearchQuery(e(), of(100), e(), of(3), e())

        when:
        List<RentDetails> rentDetails = RentDomainRepository.filterResultsWithSearchQuery([rentDetailsWithPrice], searchQuery)

        then:
        !rentDetails.empty
    }

    def "filterResultsWithSearchQuery should exclude rent details price less than search query minPrice"() {
        given:
        SearchQuery searchQuery = new SearchQuery(e(), of(210), of(5000), of(3), e())

        when:
        List<RentDetails> rentDetails = RentDomainRepository.filterResultsWithSearchQuery([rentDetailsWithPrice], searchQuery)

        then:
        rentDetails.empty
    }

    def "filterResultsWithSearchQuery should exclude rent details price greater than search query maxPrice"() {
        given:
        SearchQuery searchQuery = new SearchQuery(e(), of(100), of(190), of(3), e())

        when:
        List<RentDetails> rentDetails = RentDomainRepository.filterResultsWithSearchQuery([rentDetailsWithPrice], searchQuery)

        then:
        rentDetails.empty
    }
}
