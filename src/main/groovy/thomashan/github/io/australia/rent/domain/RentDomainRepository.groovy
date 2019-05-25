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
            RentListPage rentListPage = (RentListPage) page

            int pageEnd = rentListPage.pageEnd

            (1..pageEnd).each { pageNumber ->
                RentListPage currentPage = to(RentListPage, searchQuery, page: pageNumber)
                println("currentUrl: ${currentUrl}")

                List<RentDetails> rentailDetails = getRentDetails(currentPage)
                result = result + rentailDetails
            }
        }

        LocalDateTime end = LocalDateTime.now()
        println("start: ${start}, end: ${end}")
        println("duration ${Duration.between(start, end)}")

        return result
    }

    private List<RentDetails> getRentDetails(RentListPage rentListPage) {
        try {
            return rentListPage.rentDetails
        } catch (Exception ex) {
            rentListPage.driver.navigate().refresh()

            return getRentDetails(rentListPage)
        }
    }
}
