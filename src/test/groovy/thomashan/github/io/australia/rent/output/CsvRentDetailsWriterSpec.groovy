package thomashan.github.io.australia.rent.output

import spock.lang.Specification
import thomashan.github.io.australia.rent.DataSource
import thomashan.github.io.australia.rent.LatLongCoordinates
import thomashan.github.io.australia.rent.RentDetails

import static java.util.Collections.emptyMap as em
import static thomashan.github.io.australia.rent.DataSource.DOMAIN
import static thomashan.github.io.australia.rent.DataSource.REAL_ESTATE

class CsvRentDetailsWriterSpec extends Specification {
    private CsvRentDetailsWriter csvRentDetailsWriter = new CsvRentDetailsWriter()
    private static String header = "price,bedrooms,bathrooms,parking,address,suburb,state,postcode,latitude,longitude,daysInMarket"

    def "should generate csv file header"() {
        given:
        List<RentDetails> rentDetails = []

        when:
        File file = csvRentDetailsWriter.file(rentDetails)

        then:
        file.readLines() == [header]
        file.deleteOnExit()
    }

    def "should generate csv body"() {
        given:
        EnumMap<DataSource, Long> daysInMarket = new EnumMap<>(DataSource)
        daysInMarket.put(DOMAIN, 10)
        daysInMarket.put(REAL_ESTATE, 20)
        List<RentDetails> rentDetails = [new RentDetails(Optional.empty(), "anonAddress", "anonSuburb", "anonState", "anonPostcode", 0, 0, 0, Optional.of(new LatLongCoordinates(0, 0)), daysInMarket)]

        when:
        File file = csvRentDetailsWriter.file(rentDetails)

        then:
        file.readLines() == [header, ',0,0,0,"anonAddress","anonSuburb","anonState","anonPostcode",0.0,0.0,"DOMAIN:10|REAL_ESTATE:20"']
        file.deleteOnExit()
    }

    def "should generate csv body with no price"() {
        given:
        List<RentDetails> rentDetails = [new RentDetails(Optional.empty(), "anonAddress", "anonSuburb", "anonState", "anonPostcode", 0, 0, 0, Optional.of(new LatLongCoordinates(0, 0)), em())]

        when:
        File file = csvRentDetailsWriter.file(rentDetails)

        then:
        file.readLines() == [header, ',0,0,0,"anonAddress","anonSuburb","anonState","anonPostcode",0.0,0.0,""']
        file.deleteOnExit()
    }

    def "should generate csv body with empty latitude, longitude"() {
        given:
        List<RentDetails> rentDetails = [new RentDetails(Optional.of(100), "anonAddress", "anonSuburb", "anonState", "anonPostcode", 0, 0, 0, Optional.empty(), em())]

        when:
        File file = csvRentDetailsWriter.file(rentDetails)

        then:
        file.readLines() == [header, '100,0,0,0,"anonAddress","anonSuburb","anonState","anonPostcode",,,""']
        file.deleteOnExit()
    }
}
