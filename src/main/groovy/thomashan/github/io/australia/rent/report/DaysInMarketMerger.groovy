package thomashan.github.io.australia.rent.report

import thomashan.github.io.australia.rent.DataSource
import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.file.FileInformation

import java.time.LocalDate
import java.time.Period
import java.util.Map.Entry
import java.util.stream.Collectors

import static thomashan.github.io.australia.rent.DataSource.DOMAIN
import static thomashan.github.io.australia.rent.report.ReportType.COMMON

class DaysInMarketMerger {
    private final ReportRepository reportRepository

    DaysInMarketMerger(ReportRepository reportRepository) {
        this.reportRepository = reportRepository
    }

    List<RentDetails> mergeDaysInMarket(List<RentDetails> rentDetails, String baseDir, LocalDate previousLocalDate, LocalDate localDate) {
        Map<RentDetails, Boolean> completedEmptyDaysInMarket = [:]
        Map<RentDetails, Long> emptyDaysInMarket = [:]
        Map<RentDetails, Long> existingDaysInMarket = [:]
        rentDetails.stream()
                .forEach({
                    if (it.daysInMarket.isEmpty()) {
                        emptyDaysInMarket.put(it, 1)
                        completedEmptyDaysInMarket.put(it, false)
                    } else {
                        existingDaysInMarket[it] = it.daysInMarket[DOMAIN] + Period.between(previousLocalDate, localDate).days
                    }
                })

        List<FileInformation> fileInformationList = reportRepository.list(baseDir, COMMON)
                .collect(Collectors.toList())

        for (FileInformation fileInformation : fileInformationList) {
            Map<RentDetails, Long> previousRentDetailsMap = reportRepository.getRentDetails(fileInformation)
                    .collect(Collectors.toMap({ it }, (RentDetails rent) -> rent.daysInMarket))
            Set<RentDetails> previousRentDetails = previousRentDetailsMap.entrySet().stream()
                    .map(Entry::getKey)
                    .collect(Collectors.toSet())
            ReportName reportName = ReportName.getReportName(fileInformation)

            emptyDaysInMarket.entrySet().stream()
                    .forEach(rentDetail -> {
                        if (previousRentDetails.contains(rentDetail.key)) {
                            Long existingDaysInMarketValue = Optional.ofNullable(previousRentDetailsMap[rentDetail.key][DOMAIN]).orElse(0)
                            emptyDaysInMarket[rentDetail.key] = existingDaysInMarketValue + Period.between(reportName.localDate, localDate).days
                        } else {
                            completedEmptyDaysInMarket[rentDetail.key] = true
                        }
                    })

            if (completedMerged(completedEmptyDaysInMarket)) {
                break
            }
        }

        return rentDetails.stream()
                .map(rentDetail -> {
                    Map<DataSource, Long> daysInMarket = new EnumMap<>(DataSource)
                    if (emptyDaysInMarket.containsKey(rentDetail)) {
                        daysInMarket.put(DOMAIN, emptyDaysInMarket[rentDetail])
                    } else {
                        daysInMarket.put(DOMAIN, existingDaysInMarket[rentDetail])
                    }
                    return rentDetail.copyWith(daysInMarket: daysInMarket)
                })
                .collect(Collectors.toList())
    }

    private boolean completedMerged(Map<RentDetails, Boolean> completedEmptyDaysInMarket) {
        completedEmptyDaysInMarket.entrySet().stream()
                .map({ it.value })
                .allMatch({ it })
    }
}
