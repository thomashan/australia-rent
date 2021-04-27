package thomashan.github.io.australia.rent.geocode.google

import spock.lang.Specification
import thomashan.github.io.australia.rent.LatLongCoordinates
import thomashan.github.io.australia.rent.RentDetails

import static java.util.Collections.emptyMap as em
import static java.util.Optional.empty as e

class GoogleGeocoderSpec extends Specification {
    private GoogleGeocoder geocoder = new GoogleGeocoder()

    def "should drop the unit if the initial geocoding was not successful"() {
        given:
        RentDetails rentDetails1 = new RentDetails(e(), "2/606 Waverley Rd", "GLEN WAVERLEY", "VIC", "3150", 0, 0, 0, e(), em())
        List<RentDetails> initialRentDetails = [rentDetails1]

        when:
        List<RentDetails> resultantRentDetails = geocoder.geocode(initialRentDetails)
        RentDetails resultantRentDetails1 = resultantRentDetails[0]

        then:
        resultantRentDetails1.coordinates.get().latitude == -37.8877464
        resultantRentDetails1.coordinates.get().longitude == 145.1520288
    }

    def "should populate latitude and longitude if initial latitude longitude is empty"() {
        given:
        RentDetails rentDetails1 = new RentDetails(e(), "1 Collins St", "Melbourne", "VIC", "3000", 0, 0, 0, e(), em())
        List<RentDetails> initialRentDetails = [rentDetails1]

        when:
        List<RentDetails> resultantRentDetails = geocoder.geocode(initialRentDetails)
        RentDetails resultantRentDetails1 = resultantRentDetails[0]

        then:
        resultantRentDetails1.coordinates.get().latitude == -37.8138267
        resultantRentDetails1.coordinates.get().longitude == 144.973728
    }

    def "should not hit google api if latitude and longitude exists"() {
        given:
        RentDetails rentDetails1 = new RentDetails(e(), "1 Collins St", "Melbourne", "VIC", "3000", 0, 0, 0, Optional.of(new LatLongCoordinates(-37.8138267, 144.973728)), em())
        List<RentDetails> initialRentDetails = [rentDetails1]

        when:
        List<RentDetails> resultantRentDetails = geocoder.geocode(initialRentDetails)
        RentDetails resultantRentDetails1 = resultantRentDetails[0]

        then:
        resultantRentDetails1.coordinates.get().latitude == -37.8138267
        resultantRentDetails1.coordinates.get().longitude == 144.973728
    }

    def "geocode failing address"() {
        given:
        List<RentDetails> initialRentDetails = [new RentDetails(e(), "227 Clayton Road", "CLAYTON", "VIC", "3168", 0, 0, 0, e(), em())]

        when:
        List<RentDetails> resultantRentDetails = geocoder.geocode(initialRentDetails)
        RentDetails resultantRentDetails1 = resultantRentDetails[0]

        then:
        resultantRentDetails1.coordinates.get().latitude == -37.917253
        resultantRentDetails1.coordinates.get().longitude == 145.121029
    }
}
