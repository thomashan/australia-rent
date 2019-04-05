package thomashan.github.io.australia.rent.output

import spock.lang.Specification
import thomashan.github.io.australia.rent.LatLongCoordinates
import thomashan.github.io.australia.rent.RentDetails

class CsvRentDetailsWriterSpec extends Specification {
    private CsvRentDetailsWriter csvRentDetailsWriter = new CsvRentDetailsWriter()
    private static String header = "price,address,suburb,state,postcode,latitude,longitude"

    def "should generate csv file header"() {
        given:
        List<RentDetails> rentDetails = []

        when:
        File file = csvRentDetailsWriter.file(rentDetails)

        then:
        file.readLines() == [header]
        file.deleteOnExit()
    }

    def "should generate csv body with no price"() {
        given:
        List<RentDetails> rentDetails = [new RentDetails(Optional.empty(), "anonAddress", "anonSuburb", "anonState", "anonPostcode", Optional.of(new LatLongCoordinates(0, 0)))]

        when:
        File file = csvRentDetailsWriter.file(rentDetails)

        then:
        file.readLines() == [header, ',anonAddress,anonSuburb,anonState,anonPostcode,0.0,0.0']
        file.deleteOnExit()
    }

    def "should generate csv body with empty latitude, longitude"() {
        given:
        List<RentDetails> rentDetails = [new RentDetails(Optional.of(100), "anonAddress", "anonSuburb", "anonState", "anonPostcode", Optional.empty())]

        when:
        File file = csvRentDetailsWriter.file(rentDetails)

        then:
        file.readLines() == [header, '100,anonAddress,anonSuburb,anonState,anonPostcode,,']
        file.deleteOnExit()
    }
}
