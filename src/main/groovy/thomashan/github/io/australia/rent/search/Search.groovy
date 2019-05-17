package thomashan.github.io.australia.rent.search

import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.SearchQuery
import thomashan.github.io.australia.rent.file.FileRepository
import thomashan.github.io.australia.rent.file.dropbox.DropboxFileRepository
import thomashan.github.io.australia.rent.input.CsvRentDetailsReader
import thomashan.github.io.australia.rent.report.ReportName

import java.time.LocalDate

trait Search {
    private FileRepository fileRepository = new DropboxFileRepository()
    private final LocalDate yesterday = LocalDate.now().minusDays(1)

    abstract SearchQuery getSearchQuery()

    abstract void search()

    List<RentDetails> getPrevious() {
        CsvRentDetailsReader csvRentDetailsReader = new CsvRentDetailsReader()
        InputStream inputStream = fileRepository.read(new ReportName(this, yesterday).value)

        if (inputStream) {
            return csvRentDetailsReader.read(inputStream)
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
