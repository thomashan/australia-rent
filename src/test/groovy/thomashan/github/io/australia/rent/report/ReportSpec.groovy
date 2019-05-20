package thomashan.github.io.australia.rent.report

import spock.lang.Specification
import thomashan.github.io.australia.rent.RentDetails

class ReportSpec extends Specification {
    private RentDetails rentDetails1
    private RentDetails rentDetails2
    private RentDetails rentDetails3
    private RentDetails rentDetails4
    private Report report1
    private Report report2

    def setup() {
        rentDetails1 = new RentDetails(Optional.of(1), "anonAddress1", "anonSuburb1", "anonState1", "anonPostcode1", 0, 0, 0, Optional.empty())
        rentDetails2 = new RentDetails(Optional.of(1), "anonAddress2", "anonSuburb2", "anonState2", "anonPostcode2", 0, 0, 0, Optional.empty())
        rentDetails3 = new RentDetails(Optional.of(1), "anonAddress3", "anonSuburb3", "anonState3", "anonPostcode3", 0, 0, 0, Optional.empty())
        rentDetails4 = new RentDetails(Optional.of(1), "anonAddress4", "anonSuburb4", "anonState4", "anonPostcode4", 0, 0, 0, Optional.empty())

        report1 = new Report([rentDetails1, rentDetails2, rentDetails3])
        report2 = new Report([rentDetails2, rentDetails3, rentDetails4])
    }

    def "oldRentDetails should return correct old rent details"() {
        when:
        List<RentDetails> oldRentDetails = report2.oldRentDetails(report1)

        then:
        oldRentDetails.contains(rentDetails1)
        !oldRentDetails.contains(rentDetails2)
        !oldRentDetails.contains(rentDetails3)
        !oldRentDetails.contains(rentDetails4)
    }

    def "commonRentDetails should return correct common rent details"() {
        when:
        List<RentDetails> commonRentDetails = report2.commonRentDetails(report1)

        then:
        !commonRentDetails.contains(rentDetails1)
        commonRentDetails.contains(rentDetails2)
        commonRentDetails.contains(rentDetails3)
        !commonRentDetails.contains(rentDetails4)
    }

    def "newRentDetails should return correct new rent details"() {
        when:
        List<RentDetails> newRentDetails = report2.newRentDetails(report1)

        then:
        !newRentDetails.contains(rentDetails1)
        !newRentDetails.contains(rentDetails2)
        !newRentDetails.contains(rentDetails3)
        newRentDetails.contains(rentDetails4)
    }
}
