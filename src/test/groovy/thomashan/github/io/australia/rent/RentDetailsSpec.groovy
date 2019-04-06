package thomashan.github.io.australia.rent

import spock.lang.Specification

class RentDetailsSpec extends Specification {
    private RentDetails rentDetails = new RentDetails(Optional.of(1), "anonAddress", "anonSuburb", "anonState", "anonPostcode", Optional.of(new LatLongCoordinates(0, 0)))

    def "equals should return true if every field is the same"() {
        when:
        RentDetails r = rentDetails.copyWith()

        then:
        r == rentDetails
    }

    def "equals should return true if every field apart from coordinates is the same"() {
        when:
        RentDetails r = rentDetails.copyWith(["coordinates": Optional.empty()])

        then:
        r == rentDetails
    }

    def "equals should return false if any field apart from coordinates is the same"() {
        when:
        RentDetails r = rentDetails.copyWith(["price": Optional.of(2)])

        then:
        r != rentDetails
    }
}
