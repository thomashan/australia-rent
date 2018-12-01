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
}
