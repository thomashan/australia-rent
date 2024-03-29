package thomashan.github.io.australia.rent

import spock.lang.Specification

import static java.util.Collections.emptyMap as em
import static java.util.Optional.of

class RentDetailsSpec extends Specification {
    private RentDetails rentDetails = new RentDetails(Optional.of(1), "anonAddress", "anonSuburb", "anonState", "anonPostcode", 0, 0, 0, Optional.of(new LatLongCoordinates(0, 0)), em())

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

    def "daysInMarket should be empty when constructing with map"() {
        when:
        RentDetails rentDetails = new RentDetails(price: of(100),
                address: "address",
                suburb: "suburb",
                state: "state",
                postcode: "postcode",
                bedrooms: 2,
                bathrooms: 2,
                parking: 2
        )

        then:
        rentDetails.daysInMarket != null
        rentDetails.daysInMarket.isEmpty()
    }
}
