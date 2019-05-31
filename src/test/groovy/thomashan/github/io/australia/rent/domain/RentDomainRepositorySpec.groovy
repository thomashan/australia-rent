package thomashan.github.io.australia.rent.domain

import geb.spock.GebReportingSpec
import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.search.SearchQuery

import static java.util.Optional.empty as e
import static java.util.Optional.of

class RentDomainRepositorySpec extends GebReportingSpec {
    private RentDomainRepository rentDomainRepository = new RentDomainRepository()

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
}
