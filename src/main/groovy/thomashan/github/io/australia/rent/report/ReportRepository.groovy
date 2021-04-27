package thomashan.github.io.australia.rent.report

import thomashan.github.io.australia.rent.RentDetails
import thomashan.github.io.australia.rent.file.FileInformation
import thomashan.github.io.australia.rent.file.FileRepository
import thomashan.github.io.australia.rent.input.CsvRentDetailsReader

import java.time.LocalDate
import java.util.stream.Collectors
import java.util.stream.Stream

import static thomashan.github.io.australia.rent.report.ReportType.ALL

class ReportRepository {
    private static final Comparator<FileInformation> FILE_NAME_COMPARATOR = (f1, f2) -> f2.fileName <=> f1.fileName
    final CsvRentDetailsReader csvRentDetailsReader = new CsvRentDetailsReader()
    final FileRepository fileRepository

    ReportRepository(FileRepository fileRepository) {
        this.fileRepository = fileRepository
    }

    Stream<FileInformation> list(String directory, ReportType reportType) {
        return fileRepository.list(directory).stream()
                .filter(a -> a.fileName.matches(reportType.fileNamePattern))
                .sorted(FILE_NAME_COMPARATOR)
    }

    List<RentDetails> getLatestRentDetails(String directory) {
        return list(directory, ALL)
                .filter(fileInformation -> fileInformation.fileName != ReportName.getFileName(LocalDate.now(), ALL))
                .findFirst()
                .map(this::getRentDetails)
                .map(stream -> stream.collect(Collectors.toList()))
                .orElse([])
    }

    Stream<RentDetails> getRentDetails(FileInformation fileInformation) {
        InputStream inputStream = fileRepository.read(fileInformation.fullPath)
        if (inputStream) {
            return csvRentDetailsReader.getStream(inputStream)
        }
        return Stream.empty()
    }
}
