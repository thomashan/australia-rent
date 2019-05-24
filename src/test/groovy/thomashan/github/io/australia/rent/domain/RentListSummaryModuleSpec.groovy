package thomashan.github.io.australia.rent.domain

import spock.lang.Specification

class RentListSummaryModuleSpec extends Specification {
    def "getValue bedrooms"() {
        when:
        Integer bedrooms = RentListSummaryModule.getValue("3\nBeds")

        then:
        bedrooms == 3
    }

    def "getValue parkings 1"() {
        when:
        Integer parking = RentListSummaryModule.getValue("-\n0 Parkings")

        then:
        parking == 0
    }

    def "getValue parkings 2"() {
        when:
        Integer parking = RentListSummaryModule.getValue("1\nParkings")

        then:
        parking == 1
    }
}
