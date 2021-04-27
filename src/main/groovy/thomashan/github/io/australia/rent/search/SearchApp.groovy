package thomashan.github.io.australia.rent.search

import thomashan.github.io.australia.rent.domain.RentDomainRepository
import thomashan.github.io.australia.rent.file.dropbox.DropboxFileRepository

import static java.util.Optional.empty as e

class SearchApp {
    static void main(String[] args) {
        new DefaultSearch(new RentDomainRepository(), new DropboxFileRepository())
                .search(new SearchQuery(e(), getMinPrice(args), getMaxPrice(args), getMinBedrooms(args), getMaxBedrooms(args)))
    }

    private static Optional<Integer> getMinPrice(String[] args) {
        return Optional.of(args[0].split("-")[0].toInteger())
    }

    private static Optional<Integer> getMaxPrice(String[] args) {
        String[] results = args[0].split("-")
        return results.length == 2 ? Optional.of(results[1].toInteger()) : Optional.empty()
    }

    private static Optional<Integer> getMinBedrooms(String[] args) {
        return Optional.of(args[1].split("-")[0])
    }

    private static Optional<Integer> getMaxBedrooms(String[] args) {
        String[] results = args[1].split("-")
        return results.length == 2 ? Optional.of(results[1].toInteger()) : Optional.empty()
    }
}
