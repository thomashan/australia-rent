package thomashan.github.io.australia.rent.report

import thomashan.github.io.australia.rent.RentDetails

class Report {
    List<RentDetails> rentDetails

    Report(List<RentDetails> rentDetails) {
        this.rentDetails = rentDetails
    }

    List<RentDetails> oldRentDetails(Report report) {
        return report.rentDetails - rentDetails
    }

    List<RentDetails> commonRentDetails(Report report) {
        return rentDetails.intersect(report.rentDetails)
    }

    List<RentDetails> newRentDetails(Report report) {
        return rentDetails - report.rentDetails
    }
}
