package thomashan.github.io.australia.rent.file.dropbox


import com.dropbox.core.DbxException
import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.util.IOUtil
import com.dropbox.core.v2.DbxClientV2
import com.dropbox.core.v2.files.FileMetadata
import com.dropbox.core.v2.files.GetMetadataErrorException
import com.dropbox.core.v2.files.UploadErrorException
import com.dropbox.core.v2.files.WriteMode
import thomashan.github.io.australia.rent.file.FileRepository

class DropboxFileRepository implements FileRepository {
    private static final String ACCESS_TOKEN = System.getenv("DROPBOX_ACCESS_TOKEN")
    private DbxClientV2 client


    DropboxFileRepository() {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("australia_rent").build()
        if(!ACCESS_TOKEN) {
            throw new RuntimeException("Please specify DROPBOX_ACCESS_TOKEN")
        }
        client = new DbxClientV2(config, ACCESS_TOKEN)
    }

    @Override
    void upload(File file, String destination) {
        file.withInputStream { InputStream inputStream ->
            IOUtil.ProgressListener progressListener = new MyProgressListener(file)

            try {
                FileMetadata metadata = client.files().uploadBuilder(destination)
                        .withMode(WriteMode.OVERWRITE)
                        .withClientModified(new Date(file.lastModified()))
                        .uploadAndFinish(inputStream, progressListener)

                println(metadata.toStringMultiline())
            } catch (UploadErrorException ex) {
                System.err.println("Error uploading to Dropbox: ${ex.getMessage()}")
            } catch (DbxException ex) {
                System.err.println("Error uploading to Dropbox: ${ex.getMessage()}")
            } catch (IOException ex) {
                System.err.println("Error reading from file '${file}': ${ex.getMessage()}")
            }
        }
    }

    @Override
    InputStream read(String fileName) {
        try {
            client.files().getMetadata(fileName)
        } catch (GetMetadataErrorException ex) {
            if (ex.errorValue.pathValue.notFound) {
                return null
            }

            throw ex
        }

        return client.files().download(fileName).inputStream
    }

    @Override
    void delete(String fileName) {
        client.files().deleteV2(fileName)
    }

    private static class MyProgressListener implements IOUtil.ProgressListener {
        private final File file

        MyProgressListener(File file) {
            this.file = file
        }

        @Override
        void onProgress(long bytesWritten) {
            printProgress(bytesWritten, file.length())
        }

        private static void printProgress(long uploaded, long size) {
            System.out.printf("Uploaded %12d / %12d bytes (%5.2f%%)\n", uploaded, size, 100 * (uploaded / (double) size))
        }
    }

}
