package thomashan.github.io.australia.rent.ranking

import geb.Browser
import groovy.transform.ToString
import org.openqa.selenium.htmlunit.HtmlUnitDriver

import static thomashan.github.io.australia.rent.output.CsvWriterHelper.*

// used to generate /resource/data/ranking/suburb/2015_rankings.csv
class RankingExtractor2019 {
    static void main(String[] args) {
        RankingExtractor2019 rankingExtractor = new RankingExtractor2019()

        rankingExtractor.extractSuburbRankings()
    }

    void extractSuburbRankings() {
        Browser.drive {
            driver = new HtmlUnitDriver()
            go("https://www.domain.com.au/liveable-melbourne/melbournes-most-liveable-suburbs-2019/melbournes-307-suburbs-ranked-for-liveability-2019-898676/")

            List<String> suburbsWithRanking = $("h3")*.text()
            suburbsWithRanking.removeAll {
                !it.matches("^([\\d])+\\..*")
            }

            List<SuburbRanking> suburbRankings = suburbsWithRanking.collect { String suburbWithRanking ->
                int ranking = (suburbWithRanking.find(/^\d+\.\s/) - ". ").toInteger()
                String suburb = suburbWithRanking.replaceFirst(/^\d+\.\s/, "").strip()
                String previousRankingString = $("h3", text: suburbWithRanking).next().text()
                if (previousRankingString.startsWith("Previous rank: ")) {
                    Optional<Integer> previousRanking = (previousRankingString - "Previous rank: ").isInteger() ? Optional.of((previousRankingString - "Previous rank: ").toInteger()) : Optional.empty()
                    new SuburbRanking(suburb: suburb, ranking: ranking, previousRanking: previousRanking)
                } else if (previousRankingString.startsWith("Previous ranking: ")) {
                    new SuburbRanking(suburb: suburb, ranking: ranking, previousRanking: Optional.of((previousRankingString - "Previous ranking: ").toInteger()))
                } else {
                    new SuburbRanking(suburb: suburb, ranking: ranking, previousRanking: Optional.empty())
                }
            }

            int threshold = 100
            List<SuburbRanking> goodSuburbs = suburbRankings.findAll {
                it.ranking < threshold || (it.previousRanking.present && it.previousRanking.get() < 100)
            }

            println("current good suburb")
            goodSuburbs.sort { it.ranking }.each { println(it) }
            println("previous good suburb")
            goodSuburbs.findAll { it.previousRanking.present }.sort { it.previousRanking.get() }.each { println(it) }

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
