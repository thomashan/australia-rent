package thomashan.github.io.australia.rent.search

import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.RentRepository
import thomashan.github.io.australia.rent.SearchQuery
import thomashan.github.io.australia.rent.domain.RentDomainRepository
import thomashan.github.io.australia.rent.file.FileRepository
import thomashan.github.io.australia.rent.file.dropbox.DropboxFileRepository
import thomashan.github.io.australia.rent.geocode.Geocoder
import thomashan.github.io.australia.rent.geocode.google.GoogleGeocoder
import thomashan.github.io.australia.rent.output.CsvRentDetailsWriter
import thomashan.github.io.australia.rent.report.ReportContents
import thomashan.github.io.australia.rent.report.ReportName

import java.time.LocalDate

import static java.util.Optional.empty

class Step1 implements Search {
    private RentRepository rentDomainRepository = new RentDomainRepository()
    private FileRepository fileRepository = new DropboxFileRepository()
    private CsvRentDetailsWriter csvRentDetailsWriter = new CsvRentDetailsWriter()
    private Geocoder geocoder = new GoogleGeocoder()
    final SearchQuery searchQuery = new SearchQuery(550, 650, 3, empty())
    private final LocalDate today = LocalDate.now()

    void search() {
        List<RentDetails> rentDetails = rentDomainRepository.findAll(searchQuery)
        ReportContents oldReport = new ReportContents(getLatest())
        ReportContents report = new ReportContents(rentDetails)

        List<RentDetails> oldRentDetails = report.oldRentDetails(oldReport)
        List<RentDetails> commonRentDetails = report.commonRentDetails(oldReport)
        List<RentDetails> newRentDetails = geocoder.geocode(report.newRentDetails(oldReport))

        File deletedEntriesReportFile = csvRentDetailsWriter.file(oldRentDetails)
        File newEntriesReportFile = csvRentDetailsWriter.file(newRentDetails)
        File reportFile = csvRentDetailsWriter.file(commonRentDetails + newRentDetails)

        fileRepository.upload(reportFile, new ReportName(this, today).fullPath)
        fileRepository.upload(deletedEntriesReportFile, new ReportName(this, today, Optional.of("deleted")).fullPath)
        fileRepository.upload(newEntriesReportFile, new ReportName(this, today, Optional.of("new")).fullPath)
    }
}
