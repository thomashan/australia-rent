package thomashan.github.io.australia.rent.search

import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.RentRepository
import thomashan.github.io.australia.rent.SearchQuery
import thomashan.github.io.australia.rent.domain.RentDomainRepository
import thomashan.github.io.australia.rent.file.FileRepository
import thomashan.github.io.australia.rent.file.dropbox.DropboxFileRepository
import thomashan.github.io.australia.rent.output.CsvRentDetailsWriter
import thomashan.github.io.australia.rent.report.Report
import thomashan.github.io.australia.rent.report.ReportName

import java.time.LocalDate

import static java.util.Optional.empty

class Step1 implements Search {
    private RentRepository rentDomainRepository = new RentDomainRepository()
    private FileRepository fileRepository = new DropboxFileRepository()
    private CsvRentDetailsWriter csvRentDetailsWriter = new CsvRentDetailsWriter()
    final SearchQuery searchQuery = new SearchQuery(550, 650, 3, empty())
    private final LocalDate today = LocalDate.now()

    void search() {
        List<RentDetails> rentDetails = rentDomainRepository.findAll(searchQuery)
        Report oldReport = new Report(getPrevious())
        Report report = new Report(rentDetails)

        File reportFile = csvRentDetailsWriter.file(report.rentDetails)
        File deletedEntriesReportFile = csvRentDetailsWriter.file(report.oldRentDetails(oldReport))
        File newEntriesReportFile = csvRentDetailsWriter.file(report.newRentDetails(oldReport))

        fileRepository.upload(reportFile, new ReportName(this, today).value)
        fileRepository.upload(deletedEntriesReportFile, new ReportName(this, today, Optional.of("deleted")).value)
        fileRepository.upload(newEntriesReportFile, new ReportName(this, today, Optional.of("new")).value)
    }
}
