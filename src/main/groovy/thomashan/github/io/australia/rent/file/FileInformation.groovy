package thomashan.github.io.australia.rent.file

import groovy.transform.Immutable

import java.time.LocalDateTime

@Immutable
class FileInformation {
    String fullPath
    String fileName
    LocalDateTime modified
}
