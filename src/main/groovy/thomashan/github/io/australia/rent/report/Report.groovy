package thomashan.github.io.australia.rent.report

import thomashan.github.io.australia.rent.RentDetails

import java.time.Instant

class Report {
    Instant start
    Instant end
    List<RentDetails> rentDetails

    List<RentDetails> oldRentDetails(Report report) {
        return rentDetails - report.rentDetails
    }

    List<RentDetails> commonRentDetails(Report report) {
        return rentDetails.intersect(report.rentDetails)
    }

    List<RentDetails> newRentDetails(Report report) {
        return report.rentDetails - rentDetails
    }
}
