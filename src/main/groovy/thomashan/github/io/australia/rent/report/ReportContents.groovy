package thomashan.github.io.australia.rent.report

import thomashan.github.io.australia.rent.RentDetails

class ReportContents {
    List<RentDetails> rentDetails

    ReportContents(List<RentDetails> rentDetails) {
        this.rentDetails = rentDetails
    }

    List<RentDetails> oldRentDetails(ReportContents report) {
        return report.rentDetails - rentDetails
    }

    List<RentDetails> commonRentDetails(ReportContents report) {
        List<RentDetails> commonRentDetails1 = rentDetails.intersect(report.rentDetails)
        List<RentDetails> commonRentDetails2 = report.rentDetails.intersect(rentDetails)

        return getCommonRentDetailsWithCoordinates(commonRentDetails1, commonRentDetails2)
    }

    private List<RentDetails> getCommonRentDetailsWithCoordinates(List<RentDetails> commonRentDetails1, List<RentDetails> commonRentDetails2) {
        if (commonRentDetails1.empty) {
            return commonRentDetails1
        }

        if (commonRentDetails1[0].coordinates.empty) {
            return commonRentDetails2
        }

        return commonRentDetails1
    }

    List<RentDetails> newRentDetails(ReportContents report) {
        return rentDetails - report.rentDetails
    }
}
