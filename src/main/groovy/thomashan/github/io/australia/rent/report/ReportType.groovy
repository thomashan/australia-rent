package thomashan.github.io.australia.rent.report

import static java.util.Optional.empty
import static java.util.Optional.of

enum ReportType {
    ALL(empty()),
    COMMON(of("common")),
    DELETED(of("deleted")),
    NEW(of("new"))

    final Optional<String> suffix
    final String fileNamePattern

    ReportType(Optional<String> suffix) {
        this.suffix = suffix
        String fileNameSuffixPattern = suffix.isEmpty() ? "\\.csv" : "_${suffix.get()}\\.csv"
        this.fileNamePattern = "\\d{8}${fileNameSuffixPattern}"
    }
}
