package thomashan.github.io.australia.rent.domain

import geb.Browser
import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.RentRepository
import thomashan.github.io.australia.rent.search.SearchQuery

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

            int pageNumber = rentListPage.pageNumber
            int pageEnd = rentListPage.pageEnd

            while (pageNumber <= pageEnd) {
                RentListPage currentPage = to(RentListPage, searchQuery, page: pageNumber)
                println("currentUrl: ${currentUrl}")

                List<RentDetails> rentailDetails = getRentDetails(currentPage)
                result = result + rentailDetails
                pageEnd = currentPage.pageEnd
                pageNumber++
            }
        }

        LocalDateTime end = LocalDateTime.now()
        println("start: ${start}, end: ${end}")
        println("duration ${Duration.between(start, end)}")

        return filterResultsWithSearchQuery(result, searchQuery)
    }

    private static List<RentDetails> filterResultsWithSearchQuery(List<RentDetails> rentDetails, SearchQuery searchQuery) {
        return rentDetails.findAll { include(it, searchQuery) }
    }

    private static boolean include(RentDetails rentDetails, SearchQuery searchQuery) {
        switch (rentDetails) {
            case { it.price.empty }:
                return true
            case { searchQuery.minPrice.present && searchQuery.minPrice.get() > it.price.get() }:
                return false
            case { searchQuery.maxPrice.present && searchQuery.maxPrice.get() < it.price.get() }:
                return false
            default:
                return true
        }
    }

    private List<RentDetails> getRentDetails(RentListPage rentListPage) {
        try {
            return rentListPage.rentDetails
        } catch (Exception ex) {
            ex.printStackTrace()
            String currentUrl = rentListPage.driver.currentUrl
            println("Error! Refreshing page: [currentUrl: ${currentUrl}]")
            rentListPage.browser.go(currentUrl)

            return getRentDetails(rentListPage)
        }
    }
}
