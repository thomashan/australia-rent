package thomashan.github.io.australia.rent.search

import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.RentRepository
import thomashan.github.io.australia.rent.SearchQuery
import thomashan.github.io.australia.rent.domain.RentDomainRepository
import thomashan.github.io.australia.rent.file.FileRepository
import thomashan.github.io.australia.rent.file.dropbox.DropboxFileRepository
import thomashan.github.io.australia.rent.output.CsvRentDetailsWriter
import thomashan.github.io.australia.rent.report.ReportName

import java.time.Instant

import static java.util.Optional.empty

class Step1 implements Search {
    private RentRepository rentDomainRepository = new RentDomainRepository()
    private FileRepository fileRepository = new DropboxFileRepository()
    private CsvRentDetailsWriter csvRentDetailsWriter = new CsvRentDetailsWriter()

    @Override
    String getName() {
        return "price_550-600_3_bedroom"
    }

    void search() {
        fileRepository.read()

        SearchQuery query1 = new SearchQuery(550, 650, 3, empty())
        List<RentDetails> rentDetails = rentDomainRepository.findAll(query1)
        File file = csvRentDetailsWriter.file(rentDetails)

        ReportName reportName = new ReportName(this, Instant.now())
        fileRepository.upload(file, reportName.value)
    }
}
