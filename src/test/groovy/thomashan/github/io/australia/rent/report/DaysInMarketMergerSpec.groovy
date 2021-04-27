package thomashan.github.io.australia.rent.report

import spock.lang.Specification
import thomashan.github.io.australia.rent.DataSource
import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.file.FileInformation

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.stream.Stream

import static java.util.Collections.emptyMap as em
import static java.util.Optional.empty as e
import static thomashan.github.io.australia.rent.DataSource.DOMAIN
import static thomashan.github.io.australia.rent.report.ReportType.COMMON

class DaysInMarketMergerSpec extends Specification {
    private static final String SUBURB = "suburb"
    private static final String STATE = "state"
    private static final String POST_CODE = "postcode"
    private DaysInMarketMerger daysInMarketMerger
    private ReportRepository reportRepository
    private LocalDateTime now = LocalDateTime.now()
    private LocalDate today = now.toLocalDate()

    def setup() {
        this.reportRepository = Stub(ReportRepository)
        this.daysInMarketMerger = new DaysInMarketMerger(reportRepository)
    }

    def "merge days in market with old reports"() {
        given:
        List<RentDetails> rentDetails = [
                creatRentDetails("1"),
                creatRentDetails("2"),
                creatRentDetails("3"),
                creatRentDetails("4"),
        ]

        FileInformation fileInformation1 = createFileInformation(today.minusDays(1))
        FileInformation fileInformation2 = createFileInformation(today.minusDays(2))
        FileInformation fileInformation3 = createFileInformation(today.minusDays(10))
        Stream<FileInformation> fileInformationStream = Stream.of(
                fileInformation1,
                fileInformation2,
                fileInformation3,
        )
        reportRepository.list("/test", COMMON) >> fileInformationStream

        Stream<RentDetails> rentDetails1 = Stream.of(
                creatRentDetails("1"),
                creatRentDetails("2", 20)
        )
        reportRepository.getRentDetails(fileInformation1) >> rentDetails1

        Stream<RentDetails> rentDetails2 = Stream.of(
                creatRentDetails("2", 19),
                creatRentDetails("3")
        )
        reportRepository.getRentDetails(fileInformation2) >> rentDetails2

        Stream<RentDetails> rentDetails3 = Stream.of(
                creatRentDetails("3"),
                creatRentDetails("4")
        )
        reportRepository.getRentDetails(fileInformation3) >> rentDetails3

        when:
        List<RentDetails> result = daysInMarketMerger.mergeDaysInMarket(rentDetails, "/test", today.plusDays(1), today)

        then:
        result[0].daysInMarket[DOMAIN] == 1
        result[1].daysInMarket[DOMAIN] == 21
        result[2].daysInMarket[DOMAIN] == 10
        result[3].daysInMarket[DOMAIN] == 10
    }

    private RentDetails creatRentDetails(String address) {
        return new RentDetails(e(), address, SUBURB, STATE, POST_CODE, 1, 1, 1, e(), em())
    }

    private RentDetails creatRentDetails(String address, Long domainDaysInMarket) {
        Map<DataSource, Long> daysInMarket = [:]
        daysInMarket[DOMAIN] = domainDaysInMarket
        return new RentDetails(e(), address, SUBURB, STATE, POST_CODE, 1, 1, 1, e(), daysInMarket)
    }

    private FileInformation createFileInformation(LocalDate localDate) {
        String fileName = "${localDate.format(ReportName.FILE_NAME_FORMAT)}_common.csv"
        return new FileInformation("/test", "/test/${fileName}", fileName, now)
    }
}
