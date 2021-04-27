package thomashan.github.io.australia.rent.search


import thomashan.github.io.australia.rent.DataSource
import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.RentRepository
import thomashan.github.io.australia.rent.file.FileRepository
import thomashan.github.io.australia.rent.geocode.Geocoder
import thomashan.github.io.australia.rent.geocode.google.GoogleGeocoder
import thomashan.github.io.australia.rent.output.CsvRentDetailsWriter
import thomashan.github.io.australia.rent.report.DaysInMarketMerger
import thomashan.github.io.australia.rent.report.ReportContents
import thomashan.github.io.australia.rent.report.ReportName
import thomashan.github.io.australia.rent.report.ReportRepository

import java.time.LocalDate
import java.util.stream.Collectors

import static thomashan.github.io.australia.rent.DataSource.DOMAIN
import static thomashan.github.io.australia.rent.report.ReportType.*

class DefaultSearch implements Search {
    private final DaysInMarketMerger daysInMarketMerger
    private final RentRepository rentRepository
    private CsvRentDetailsWriter csvRentDetailsWriter = new CsvRentDetailsWriter()
    private Geocoder geocoder = new GoogleGeocoder()
    private final LocalDate today = LocalDate.now()
    private final ReportRepository reportRepository

    DefaultSearch(RentRepository rentRepository,
                  FileRepository fileRepository) {
        this.rentRepository = rentRepository
        this.thomashan_github_io_australia_rent_search_Search__fileRepository = fileRepository
        this.reportRepository = new ReportRepository(fileRepository)
        this.daysInMarketMerger = new DaysInMarketMerger(reportRepository)
    }

    void search(SearchQuery searchQuery) {
        String baseDir = "/${searchQuery.name}"
        List<RentDetails> rentDetails = rentRepository.findAll(searchQuery)
        LocalDate previousDate = reportRepository.list(baseDir, COMMON)
                .findFirst()
                .map(ReportName::getReportName)
                .map(ReportName::getLocalDate)
                .orElse(today)
        ReportContents oldReport = new ReportContents(reportRepository.getLatestRentDetails(baseDir))
        ReportContents report = new ReportContents(rentDetails)

        List<RentDetails> oldRentDetails = populateDaysInMarketForExistingRentDetails(report.oldRentDetails(oldReport), baseDir, previousDate, today)
        List<RentDetails> commonRentDetails = populateDaysInMarketForExistingRentDetails(geocoder.geocode(report.commonRentDetails(oldReport)), baseDir, previousDate, today)
        List<RentDetails> newRentDetails = populateDaysInMarketForNewRentDetails(geocoder.geocode(report.newRentDetails(oldReport)))

        File deletedEntriesReportFile = csvRentDetailsWriter.file(oldRentDetails)
        File newEntriesReportFile = csvRentDetailsWriter.file(newRentDetails)
        File commonEntriesReportFile = csvRentDetailsWriter.file(commonRentDetails)
        File reportFile = csvRentDetailsWriter.file(commonRentDetails + newRentDetails)

        thomashan_github_io_australia_rent_search_Search__fileRepository.upload(reportFile, new ReportName(baseDir, today, ALL).fullPath)
        thomashan_github_io_australia_rent_search_Search__fileRepository.upload(commonEntriesReportFile, new ReportName(baseDir, today, COMMON).fullPath)
        thomashan_github_io_australia_rent_search_Search__fileRepository.upload(deletedEntriesReportFile, new ReportName(baseDir, today, DELETED).fullPath)
        thomashan_github_io_australia_rent_search_Search__fileRepository.upload(newEntriesReportFile, new ReportName(baseDir, today, NEW).fullPath)
    }

    private List<RentDetails> populateDaysInMarketForExistingRentDetails(List<RentDetails> rentDetails, String baseDir, LocalDate previousLocalDate, LocalDate currentDate) {
        return daysInMarketMerger.mergeDaysInMarket(rentDetails, baseDir, previousLocalDate, currentDate)
    }

    private List<RentDetails> populateDaysInMarketForNewRentDetails(List<RentDetails> newRentDetails) {
        newRentDetails.stream()
                .map(newRentDetail -> {
                    EnumMap<DataSource, Long> daysInMarket = new EnumMap<>(DataSource)
                    daysInMarket.put(DOMAIN, 1)
                    newRentDetail.copyWith(daysInMarket: daysInMarket)
                })
                .collect(Collectors.toList())
    }
}
