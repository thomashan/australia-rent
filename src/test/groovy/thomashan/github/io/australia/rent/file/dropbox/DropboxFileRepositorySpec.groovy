package thomashan.github.io.australia.rent.file.dropbox

import spock.lang.Specification
import thomashan.github.io.australia.rent.file.FileInformation

class DropboxFileRepositorySpec extends Specification {
    private DropboxFileRepository dropboxFileRepository = new DropboxFileRepository()
    private static final String basePath = "/test"

    def cleanupSpec() {
        new DropboxFileRepository().delete(basePath)
    }

    def "should upload file dropbox"() {
        given:
        String fileName = "${basePath}/test.csv"
        File file = new File(this.class.getResource("/rent_details.csv").toURI())

        when:
        dropboxFileRepository.upload(file, fileName)

        then:
        dropboxFileRepository.read(fileName) != null
    }

    def "list should return empty fileInformation if directory does not exist"() {
        when:
        List<FileInformation> files = dropboxFileRepository.list("${basePath}1")

        then:
        files.empty
    }

    def "list should list files"() {
        String fileName = "${basePath}/test.csv"
        File file = new File(this.class.getResource("/rent_details.csv").toURI())
        dropboxFileRepository.upload(file, fileName)

        when:
        List<FileInformation> files = dropboxFileRepository.list(basePath)

        then:
        !files.empty
        files[0].fullPath == fileName
    }
}
