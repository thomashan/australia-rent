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

        then:
        list[0].price == '$600'
        list[0].addressLine1 == '48 William Street'
        list[0].suburb == 'ABBOTSFORD'
        list[0].state == 'VIC'
        list[0].postcode == '3067'
    }
}
