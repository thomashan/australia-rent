package thomashan.github.io.australia.rent.search

import static java.util.Optional.empty as e
import static java.util.Optional.of

class SearchApp {
    static void main(String[] args) {
        new Step1(new SearchQuery(e(), of(400), of(450), of(3), of(3))).search()
        new Step1(new SearchQuery(e(), of(450), of(500), of(3), of(3))).search()
        new Step1(new SearchQuery(e(), of(500), of(550), of(3), of(3))).search()
        new Step1(new SearchQuery(e(), of(550), of(600), of(3), of(3))).search()
        new Step1(new SearchQuery(e(), of(600), of(650), of(3), of(3))).search()
        new Step1(new SearchQuery(e(), of(650), of(700), of(3), of(3))).search()
        new Step1(new SearchQuery(e(), of(700), of(750), of(3), e())).search()
    }
}
