package thomashan.github.io.australia.rent.search

import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.SearchQuery
import thomashan.github.io.australia.rent.file.FileInformation
import thomashan.github.io.australia.rent.file.FileRepository
import thomashan.github.io.australia.rent.file.dropbox.DropboxFileRepository
import thomashan.github.io.australia.rent.input.CsvRentDetailsReader
import thomashan.github.io.australia.rent.report.ReportName

import java.time.LocalDate

trait Search {
    private FileRepository fileRepository = new DropboxFileRepository()
    private final LocalDate today = LocalDate.now()

    abstract SearchQuery getSearchQuery()

    abstract void search()

    List<RentDetails> getLatest() {
        ReportName todaysReport = new ReportName(this, today)
        List<FileInformation> fileInformationList = fileRepository.list(todaysReport.base)

        FileInformation latestFileInformation = fileInformationList
                .findAll { it.fileName.matches(todaysReport.fileNamePattern) }
                .findAll { it.fileName != todaysReport.fileName }
                .max { it.modified }

        if (latestFileInformation) {
            InputStream inputStream = fileRepository.read(latestFileInformation.fullPath)

            if (inputStream) {
                CsvRentDetailsReader csvRentDetailsReader = new CsvRentDetailsReader()

                return csvRentDetailsReader.read(inputStream)
            }
        }

        return []
    }

    String getName() {
        return "prices_${prices}_bedrooms_${bedrooms}"
    }

    String getPrices() {
        return "${searchQuery.minPrice}-${searchQuery.maxPrice}"
    }

    String getBedrooms() {
        if (searchQuery.maxBedroom.isEmpty()) {
            return "${searchQuery.minBedroom}+"
        }

        return "${searchQuery.minBedroom}-${searchQuery.maxBedroom.get()}"
    }
}
