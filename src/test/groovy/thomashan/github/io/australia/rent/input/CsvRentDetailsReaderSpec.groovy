package thomashan.github.io.australia.rent.input

import spock.lang.Specification
import thomashan.github.io.australia.rent.DataSource
import thomashan.github.io.australia.rent.RentDetails

import java.util.stream.Collectors
import java.util.stream.Stream

import static thomashan.github.io.australia.rent.DataSource.DOMAIN
import static thomashan.github.io.australia.rent.DataSource.REAL_ESTATE

class CsvRentDetailsReaderSpec extends Specification {
    private CsvRentDetailsReader csvRentDetailsReader = new CsvRentDetailsReader()

    def "should return stream of rent details from csv file"() {
        when:
        Stream<RentDetails> rentDetailsStream = csvRentDetailsReader.getStream(this.class.getResourceAsStream("/rent_details.csv"))
        List<RentDetails> rentDetails = rentDetailsStream.collect(Collectors.toList())
        RentDetails rentDetail = rentDetails[0]

        then:
        rentDetails.size() == 1
        rentDetail.price.get() == 0
        rentDetail.bedrooms == 0
        rentDetail.bathrooms == 0
        rentDetail.parking == 0
        rentDetail.address == "anonAddress"
        rentDetail.suburb == "anonSuburb"
        rentDetail.state == "anonState"
        rentDetail.postcode == "anonPostcode"
        rentDetail.coordinates.get().latitude == 0
        rentDetail.coordinates.get().longitude == 0
    }

    def "should return list of rent details from csv file"() {
        when:
        List<RentDetails> rentDetails = csvRentDetailsReader.getList(this.class.getResourceAsStream("/rent_details.csv"))
        RentDetails rentDetail = rentDetails[0]

        then:
        rentDetails.size() == 1
        rentDetail.price.get() == 0
        rentDetail.bedrooms == 0
        rentDetail.bathrooms == 0
        rentDetail.parking == 0
        rentDetail.address == "anonAddress"
        rentDetail.suburb == "anonSuburb"
        rentDetail.state == "anonState"
        rentDetail.postcode == "anonPostcode"
        rentDetail.coordinates.get().latitude == 0
        rentDetail.coordinates.get().longitude == 0
    }

    def "should return list of rent details from csv file with daysInMarket"() {
        when:
        List<RentDetails> rentDetails = csvRentDetailsReader.getList(this.class.getResourceAsStream("/rent_details_with_daysInMarket.csv"))
        RentDetails rentDetail = rentDetails[0]
        Map<DataSource, Long> expectedDaysInMarket = new EnumMap<>(DataSource)
        expectedDaysInMarket[DOMAIN] = 1
        expectedDaysInMarket[REAL_ESTATE] = 2

        then:
        rentDetails.size() == 1
        rentDetail.price.get() == 0
        rentDetail.bedrooms == 0
        rentDetail.bathrooms == 0
        rentDetail.parking == 0
        rentDetail.address == "anonAddress"
        rentDetail.suburb == "anonSuburb"
        rentDetail.state == "anonState"
        rentDetail.postcode == "anonPostcode"
        rentDetail.coordinates.get().latitude == 0
        rentDetail.coordinates.get().longitude == 0
        rentDetail.daysInMarket == expectedDaysInMarket
    }

    def "should return rent details with empty price"() {
        when:
        List<RentDetails> rentDetails = csvRentDetailsReader.getList(this.class.getResourceAsStream("/rent_details_empty_price.csv"))

        then:
        rentDetails[0].price.empty
    }

    def "should return rent details with empty coordinates"() {
        when:
        List<RentDetails> rentDetails = csvRentDetailsReader.getList(this.class.getResourceAsStream("/rent_details_empty_coordinates.csv"))

        then:
        rentDetails[0].coordinates.empty
    }

    def "should return rent details with empty latitude"() {
        when:
        List<RentDetails> rentDetails = csvRentDetailsReader.getList(this.class.getResourceAsStream("/rent_details_empty_latitude.csv"))

        then:
        rentDetails[0].coordinates.empty
    }

    def "should return rent details with empty longitude"() {
        when:
        List<RentDetails> rentDetails = csvRentDetailsReader.getList(this.class.getResourceAsStream("/rent_details_empty_longitude.csv"))

        then:
        rentDetails[0].coordinates.empty
    }
}
