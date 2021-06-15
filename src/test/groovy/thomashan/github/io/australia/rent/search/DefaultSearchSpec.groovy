package thomashan.github.io.australia.rent.search

import spock.lang.Specification
import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.RentRepository
import thomashan.github.io.australia.rent.file.FileRepository
import thomashan.github.io.australia.rent.input.CsvRentDetailsReader

import static java.util.Collections.emptyMap as em
import static java.util.Optional.empty as e
import static thomashan.github.io.australia.rent.DataSource.DOMAIN

class DefaultSearchSpec extends Specification {
    private static final String SUBURB = "suburb"
    private static final String STATE = "state"
    private static final String POST_CODE = "postcode"
    private RentRepository rentRepository
    private FileRepository fileRepository
    private DefaultSearch defaultSearch
    private CsvRentDetailsReader csvRentDetailsReader


    def setup() {
        this.rentRepository = Stub(RentRepository)
        this.fileRepository = Mock(FileRepository)
        this.defaultSearch = new DefaultSearch(rentRepository, fileRepository)
        this.csvRentDetailsReader = new CsvRentDetailsReader()
    }

    def "new rent details daysInMarket is populated"() {
        given:
        fileRepository.list(_) >> []
        List<RentDetails> rentDetails = [
                new RentDetails(e(), "1", SUBURB, STATE, POST_CODE, 1, 1, 1, e(), em())
        ]
        rentRepository.findAll(_) >> rentDetails

        when:
        defaultSearch.search(new SearchQuery(e(), e(), e(), e(), e()))

        then:
        4 * fileRepository.upload({ File file ->
            List<RentDetails> result = csvRentDetailsReader.getList(file.newInputStream())
            if (result) {
                assert result[0].daysInMarket[DOMAIN] == 1
            }
        }, _)
    }
}
