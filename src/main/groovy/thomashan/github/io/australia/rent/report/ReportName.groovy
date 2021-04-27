package thomashan.github.io.australia.rent.report

import thomashan.github.io.australia.rent.file.FileInformation

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ReportName {
    static final FILE_NAME_FORMAT = DateTimeFormatter.BASIC_ISO_DATE
    final String base
    final String fullPath
    final String fileName
    final Optional<String> suffix
    final LocalDate localDate

    ReportName(String base, LocalDate localDate, ReportType reportType) {
        this.base = base
        this.suffix = reportType.getSuffix()
        this.fileName = getFileName(localDate, reportType)
        this.fullPath = "${base}/${fileName}"
        this.localDate = localDate
    }

    static String getFileName(LocalDate localDate, ReportType reportType) {
        String fileNamePrefix = "${localDate.format(FILE_NAME_FORMAT)}"
        String fileNameSuffix = reportType.suffix
                .map(suffix -> "_${suffix}.csv")
                .orElse(".csv")

        return fileNamePrefix + fileNameSuffix
    }

    static ReportName getReportName(FileInformation fileInformation) {
        String[] fileNameArray = (fileInformation.fileName - ".csv").split("_")
        LocalDate parsedLocalDate = LocalDate.parse(fileNameArray[0], FILE_NAME_FORMAT)
        ReportType parsedReportType = getReportType(fileNameArray)
        return new ReportName(fileInformation.baseDir, parsedLocalDate, parsedReportType)
    }

    private static ReportType getReportType(String[] fileNameArray) {
        if (fileNameArray.size() == 1) {
            return ReportType.ALL
        }
        return ReportType.valueOf((fileNameArray[1] - "_").toUpperCase())
    }
}
