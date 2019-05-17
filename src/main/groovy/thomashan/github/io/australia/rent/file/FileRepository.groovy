package thomashan.github.io.australia.rent.file

trait FileRepository {
    abstract void upload(File file, String destination)

    abstract InputStream read(String fileName)

    abstract void delete(String fileName)
}
