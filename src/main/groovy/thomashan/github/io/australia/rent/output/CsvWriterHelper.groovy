package thomashan.github.io.australia.rent.output

final class CsvWriterHelper {
    private static String newline = "\n"

    private CsvWriterHelper() {
        // prevent instantiation
    }

    static String q(String input) {
        return doubleQuotes(input)
    }

    static String doubleQuotes(String input) {
        return "\"${input}\""
    }

    static String h(List<String> headers) {
        return header(headers)
    }

    static String header(List<String> headers) {
        return join(headers)
    }

    static String j(List<String> line) {
        return join(line)
    }

    static String join(List<String> line) {
        return line.inject { result, field -> "${result},${field}" } + newline
    }
}
