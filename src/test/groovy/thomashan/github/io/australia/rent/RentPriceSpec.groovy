package thomashan.github.io.australia.rent

import geb.spock.GebSpec
import thomashan.github.io.australia.rent.domain.RentListPage

class RentPriceSpec extends GebSpec {
    def "get the domain.com.au rent price"() {
        when:
        to RentListPage

        then:
        rentDetails[0].price.get() >= 550 && rentDetails[0].price.get() <= 600
    }
}
