package thomashan.github.io.australia.rent.ranking

import geb.Browser
import groovy.transform.ToString
import org.openqa.selenium.htmlunit.HtmlUnitDriver

import java.util.regex.Matcher

import static thomashan.github.io.australia.rent.output.CsvWriterHelper.*

// used to generate /resource/data/ranking/suburb/rankings.csv
class RankingExtractor {
    static void main(String[] args) {
        RankingExtractor rankingExtractor = new RankingExtractor()

        rankingExtractor.extractSuburbRankings()
    }

    void extractSuburbRankings() {
        Browser.drive {
            driver = new HtmlUnitDriver()
            go("https://www.domain.com.au/news/melbournes-321-suburbs-ranked-for-liveability-20151106-gkq447/")

            List<String> suburbs = $("h3")*.text()
            suburbs.removeAll {
                it.startsWith("Ranked") || it.startsWith("Previous") || it.startsWith("Article Search") || it.startsWith("Get the latest updates with our newsletter")
            }

            List<SuburbRanking> suburbRankings = suburbs.collect { String suburb ->
                String rankings = $("h3", text: suburb).next().text()
                Matcher matcher = rankings =~ /(?ms)Ranked:\s?(\d+)(?:(?:.*)(?:Previous rank)(?:ing)?:\s?(\d+|–))?/
                assert matcher.count > 0
                List<String> results = matcher[0]
                Integer ranking = results[1].toInteger()
                Optional previousRanking = results[2] && results[2].isInteger() ? Optional.of(results[2].toInteger()) : Optional.empty()

                new SuburbRanking(suburb: suburb, ranking: ranking, previousRanking: previousRanking)
            }

            Integer threshold = 100
            List<SuburbRanking> goodSuburbs = suburbRankings.findAll {
                it.ranking < threshold || (it.previousRanking.present && it.previousRanking.get() < 100)
            }

            print(getHeaderLine())
            suburbRankings.each {
                print(line(it))
            }

        }
    }

    private String line(SuburbRanking suburbRanking) {
        return j([q(suburbRanking.suburb),
                  suburbRanking.ranking,
                  previousRanking(suburbRanking)])
    }

    private String previousRanking(SuburbRanking suburbRanking) {
        return suburbRanking.previousRanking.present ? suburbRanking.previousRanking.get() : ""
    }

    private String getHeaderLine() {
        return h(["suburb", "ranking", "previous_ranking"])
    }

    @ToString
    private static class SuburbRanking {
        String suburb
        Integer ranking
        Optional<Integer> previousRanking
    }
}
