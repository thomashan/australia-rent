package thomashan.github.io.australia.rent.file.dropbox

import spock.lang.Specification

class DropboxFileRepositorySpec extends Specification {
    private DropboxFileRepository dropboxFileRepository = new DropboxFileRepository()
    private static final String basePath = "/test"

    def cleanupSpec() {
        new DropboxFileRepository().delete(basePath)
    }

    def "should write file dropbox"() {
        given:
        String fileName = "${basePath}/test.csv"
        File file = new File(this.class.getResource("/rent_details.csv").toURI())

        when:
        dropboxFileRepository.upload(file, fileName)

        then:
        dropboxFileRepository.read(fileName) != null
        dropboxFileRepository.delete(fileName)
    }
}
