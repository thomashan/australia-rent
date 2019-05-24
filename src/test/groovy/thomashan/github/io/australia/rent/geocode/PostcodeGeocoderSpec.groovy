package thomashan.github.io.australia.rent.geocode

import com.google.common.geometry.S2LatLng
import spock.lang.Specification

import static thomashan.github.io.australia.rent.geocode.PostcodeGeocoder.postcode

class PostcodeGeocoderSpec extends Specification {
    def "should return S2Point from known post code"() {
        when:
        Optional<S2LatLng> point = postcode(3000)

        then:
        point.get().latDegrees() == -37.814563
        point.get().lngDegrees() == 144.970267
    }

    def "should return nothing if post code is unknown"() {
        when:
        Optional<S2LatLng> point = postcode(0)

        then:
        point.empty
    }
}
