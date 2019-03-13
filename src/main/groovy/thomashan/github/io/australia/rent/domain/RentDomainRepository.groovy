package thomashan.github.io.australia.rent.domain

import geb.Browser
import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.RentRepository
import thomashan.github.io.australia.rent.SearchQuery

import java.time.Duration
import java.time.LocalDateTime

class RentDomainRepository implements RentRepository {
    @Override
    List<RentDetails> findAll(SearchQuery searchQuery) {
        LocalDateTime start = LocalDateTime.now()
        List<RentDetails> result = []
        Browser.drive {
            to RentListPage, searchQuery

            int pageEnd = page.pageEnd

            (1..pageEnd).each { pageNumber ->
                to RentListPage, searchQuery, page: pageNumber
                println("currentUrl: ${currentUrl}")

                try {
                    List<RentDetails> rentailDetails = page.rentDetails
                    result = result + rentailDetails
                } catch (ex) {
                    ex.printStackTrace()
                    println("failed at ${currentUrl}")
                }

            }
        }

        LocalDateTime end = LocalDateTime.now()
        println("start: ${start}, end: ${end}")
        println("duration ${Duration.between(start, end)}")

        return result
    }
}
