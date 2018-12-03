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

    def "get the correct information"() {
        when:
        to RentListPage
        List<RentDetails> rentDetails = list.collect {
            new RentDetails(price: it.price, address: it.addressLine1, suburb: it.suburb, state: it.state, postcode: it.postcode)
        }

        then:
        rentDetails[0].price == 600
        rentDetails[0].address == "48 William Street"
        rentDetails[0].suburb == "ABBOTSFORD"
        rentDetails[0].state == "VIC"
        rentDetails[0].postcode == "3067"
    }
}
