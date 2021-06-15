package thomashan.github.io.australia.rent.report

import spock.lang.Specification
import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.file.FileInformation
import thomashan.github.io.australia.rent.file.FileRepository

import java.nio.file.Files
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.stream.Collectors

import static thomashan.github.io.australia.rent.report.ReportName.FILE_NAME_FORMAT
import static thomashan.github.io.australia.rent.report.ReportType.ALL
import static thomashan.github.io.australia.rent.report.ReportType.COMMON

class ReportRepositorySpec extends Specification {
    private ReportRepository reportRepository
    private FileRepository fileRepository
    private String directory = "baseDir"
    private LocalDateTime now = LocalDateTime.now()
    private LocalDate today = now.toLocalDate()

    def setup() {
        fileRepository = Stub(FileRepository)
        reportRepository = new ReportRepository(fileRepository)
    }

    def "test list FileInformation with suffix"() {
        given:
        List<FileInformation> response = [
                new FileInformation(directory, "$directory/20200101.csv", "20200101.csv", now),
                new FileInformation(directory, "$directory/20200101_common.csv", "20200101_common.csv", now),
                new FileInformation(directory, "$directory/20200102_common.csv", "20200102_common.csv", now),
                new FileInformation(directory, "$directory/20200101_new.csv", "20200101_new.csv", now),
                new FileInformation(directory, "$directory/20200101_deleted.csv", "20200101_deleted.csv", now),
        ]
        fileRepository.list(directory) >> response

        when:
        List<FileInformation> fileInformation = reportRepository.list(directory, COMMON).collect(Collectors.toList())

        then:
        2 == fileInformation.size()
        "20200102_common.csv" == fileInformation[0].fileName
        "20200101_common.csv" == fileInformation[1].fileName
    }

    def "test list FileInformation without suffix"() {
        given:
        List<FileInformation> response = [
                new FileInformation(directory, "$directory/20200101.csv", "20200101.csv", now),
                new FileInformation(directory, "$directory/20200101_common.csv", "20200101_common.csv", now),
                new FileInformation(directory, "$directory/20200102_common.csv", "20200102_common.csv", now),
                new FileInformation(directory, "$directory/20200101_new.csv", "20200101_new.csv", now),
                new FileInformation(directory, "$directory/20200101_deleted.csv", "20200101_deleted.csv", now),
        ]
        fileRepository.list(directory) >> response

        when:
        List<FileInformation> fileInformation = reportRepository.list(directory, ALL).collect(Collectors.toList())

        then:
        1 == fileInformation.size()
        "20200101.csv" == fileInformation[0].fileName
    }

    def "getLatest returns the latest available from fileRepository which is not today's list"() {
        given:
        File oneEntryFile = new File(this.class.getResource("/rent_details.csv").toURI())
        File threeEntryFile = new File(this.class.getResource("/rent_details_3_entries.csv").toURI())

        File destinationDir = File.createTempDir()
        File file1 = new File(fullPath(destinationDir.toString(), today.minusDays(2)))
        File file2 = new File(fullPath(destinationDir.toString(), today.minusDays(1)))
        File file3 = new File(fullPath(destinationDir.toString(), today))

        Files.copy(oneEntryFile.toPath(), file1.toPath())
        Files.copy(threeEntryFile.toPath(), file2.toPath())
        Files.copy(oneEntryFile.toPath(), file3.toPath())

        List<FileInformation> response = [
                new FileInformation(destinationDir.toString(), fullPath(destinationDir.toString(), today), fileName(today), now),
                new FileInformation(destinationDir.toString(), fullPath(destinationDir.toString(), today.minusDays(1)), fileName(today.minusDays(1)), now),
                new FileInformation(destinationDir.toString(), fullPath(destinationDir.toString(), today.minusDays(2)), fileName(today.minusDays(2)), now),
        ]
        fileRepository.list(directory) >> response

        destinationDir.deleteOnExit()
        file1.deleteOnExit()
        file2.deleteOnExit()
        file3.deleteOnExit()

        fileRepository.read(file2.toString()) >> file2.newInputStream()

        when:
        List<RentDetails> rentDetails = reportRepository.getLatestRentDetails(directory)

        then:
        rentDetails.size() == 3
    }

    def "getRentDetails should return a stream of rent details"() {
        given:
        File oneEntryFile = new File(this.class.getResource("/rent_details.csv").toURI())

        File destinationDir = File.createTempDir()
        File file = new java.io.File(fullPath(destinationDir.toString(), today))
        Files.copy(oneEntryFile.toPath(), file.toPath())

        fileRepository.read(file.toString()) >> file.newInputStream()

        destinationDir.deleteOnExit()
        file.deleteOnExit()

        when:
        List<RentDetails> rentDetails = reportRepository.getRentDetails(new FileInformation(destinationDir.toString(), fullPath(destinationDir.toString(), today), fileName(today), now))
                .collect(Collectors.toList())

        then:
        rentDetails.size() == 1
    }

    private String fullPath(String basePath, LocalDate localDate) {
        return "${basePath}/${fileName(localDate)}"
    }

    private String fileName(LocalDate localDate) {
        return "${localDate.format(FILE_NAME_FORMAT)}.csv"
    }
}
