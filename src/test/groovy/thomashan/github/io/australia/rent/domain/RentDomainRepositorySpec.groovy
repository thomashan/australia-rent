package thomashan.github.io.australia.rent.domain

import geb.spock.GebReportingSpec
import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.SearchQuery

import static java.util.Optional.empty

class RentDomainRepositorySpec extends GebReportingSpec {
    private RentDomainRepository rentDomainRepository = new RentDomainRepository()

    def "get all listing"() {
        when:
        List<RentDetails> rentDetails = rentDomainRepository.findAll(new SearchQuery(4500, 5000, 3, empty()))

        then:
        rentDetails.size() > 0
        println(rentDetails.size())
        rentDetails.collect { it.suburb }.toSet().sort().each {
            println(it)
        }
    }
}
