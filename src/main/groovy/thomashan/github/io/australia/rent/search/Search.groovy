package thomashan.github.io.australia.rent.search

import thomashan.github.io.australia.rent.file.FileRepository

import java.time.LocalDate

trait Search {
    private FileRepository fileRepository
    private final LocalDate today = LocalDate.now()

    abstract void search(SearchQuery searchQuery)
}
