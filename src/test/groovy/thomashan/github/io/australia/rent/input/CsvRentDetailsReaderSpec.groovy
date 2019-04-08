package thomashan.github.io.australia.rent.input

import spock.lang.Specification
import thomashan.github.io.australia.rent.RentDetails

class CsvRentDetailsReaderSpec extends Specification {
    private CsvRentDetailsReader csvRentDetailsReader = new CsvRentDetailsReader()

    def "should return list of rent details from csv file"() {
        when:
        List<RentDetails> rentDetails = csvRentDetailsReader.read(this.class.getResourceAsStream("/rent_details.csv"))
        RentDetails rentDetail = rentDetails[0]

        then:
        rentDetails.size() == 1
        rentDetail.price.get() == 0
        rentDetail.address == "anonAddress"
        rentDetail.suburb == "anonSuburb"
        rentDetail.state == "anonState"
        rentDetail.postcode == "anonPostcode"
        rentDetail.coordinates.get().latitude == 0
        rentDetail.coordinates.get().longitude == 0
    }

    def "should return rent details with empty price"() {
        when:
        List<RentDetails> rentDetails = csvRentDetailsReader.read(this.class.getResourceAsStream("/rent_details_empty_price.csv"))

        then:
        rentDetails[0].price.empty
    }

    def "should return rent details with empty coordinates"() {
        when:
        List<RentDetails> rentDetails = csvRentDetailsReader.read(this.class.getResourceAsStream("/rent_details_empty_coordinates.csv"))

        then:
        rentDetails[0].coordinates.empty
    }

    def "should return rent details with empty latitude"() {
        when:
        List<RentDetails> rentDetails = csvRentDetailsReader.read(this.class.getResourceAsStream("/rent_details_empty_latitude.csv"))

        then:
        rentDetails[0].coordinates.empty
    }

    def "should return rent details with empty longitude"() {
        when:
        List<RentDetails> rentDetails = csvRentDetailsReader.read(this.class.getResourceAsStream("/rent_details_empty_longitude.csv"))

        then:
        rentDetails[0].coordinates.empty
    }
}
