package org.jabref.logic.importer.fetcher;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jabref.logic.importer.ImportFormatPreferences;
import org.jabref.logic.importer.PagedSearchBasedFetcher;
import org.jabref.logic.importer.SearchBasedFetcher;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.StandardField;
import org.jabref.model.entry.types.StandardEntryType;
import org.jabref.testutils.category.FetcherTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@FetcherTest
class IEEETest implements SearchBasedFetcherCapabilityTest, PagedSearchFetcherTest {

    private IEEE fetcher;
    private BibEntry entry;

    @BeforeEach
    void setUp() {
        ImportFormatPreferences importFormatPreferences = mock(ImportFormatPreferences.class);
        when(importFormatPreferences.getKeywordSeparator()).thenReturn(',');
        fetcher = new IEEE(importFormatPreferences);
        entry = new BibEntry();
    }

    @Test
    void findByDOI() throws Exception {
        entry.setField(StandardField.DOI, "10.1109/ACCESS.2016.2535486");
        assertEquals(Optional.of(new URL("https://ieeexplore.ieee.org/ielx7/6287639/7419931/07421926.pdf?tp=&arnumber=7421926&isnumber=7419931&ref=")),
                fetcher.findFullText(entry));
    }

    @Test
    void findByDocumentUrl() throws Exception {
        entry.setField(StandardField.URL, "https://ieeexplore.ieee.org/document/7421926/");
        assertEquals(Optional.of(new URL("https://ieeexplore.ieee.org/ielx7/6287639/7419931/07421926.pdf?tp=&arnumber=7421926&isnumber=7419931&ref=")),
                fetcher.findFullText(entry));
    }

    @Test
    void findByURL() throws Exception {
        entry.setField(StandardField.URL, "https://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=7421926&ref=");
        assertEquals(Optional.of(new URL("https://ieeexplore.ieee.org/ielx7/6287639/7419931/07421926.pdf?tp=&arnumber=7421926&isnumber=7419931&ref=")),
                fetcher.findFullText(entry));
    }

    @Test
    void findByOldURL() throws Exception {
        entry.setField(StandardField.URL, "https://ieeexplore.ieee.org/stamp/stamp.jsp?arnumber=7421926");
        assertEquals(Optional.of(new URL("https://ieeexplore.ieee.org/ielx7/6287639/7419931/07421926.pdf?tp=&arnumber=7421926&isnumber=7419931&ref=")),
                fetcher.findFullText(entry));
    }

    @Test
    void findByDOIButNotURL() throws Exception {
        entry.setField(StandardField.DOI, "10.1109/ACCESS.2016.2535486");
        entry.setField(StandardField.URL, "http://dx.doi.org/10.1109/ACCESS.2016.2535486");
        assertEquals(Optional.of(new URL("https://ieeexplore.ieee.org/ielx7/6287639/7419931/07421926.pdf?tp=&arnumber=7421926&isnumber=7419931&ref=")),
                fetcher.findFullText(entry));
    }

    @Test
    void notFoundByURL() throws Exception {
        entry.setField(StandardField.URL, "http://dx.doi.org/10.1109/ACCESS.2016.2535486");
        assertEquals(Optional.empty(), fetcher.findFullText(entry));
    }

    @Test
    void notFoundByDOI() throws Exception {
        entry.setField(StandardField.DOI, "10.1021/bk-2006-WWW.ch014");
        assertEquals(Optional.empty(), fetcher.findFullText(entry));
    }

    @Test
    void searchResultHasNoKeywordTerms() throws Exception {
        BibEntry expected = new BibEntry(StandardEntryType.Article)
                .withField(StandardField.AUTHOR, "Shatakshi Jha and Ikhlaq Hussain and Bhim Singh and Sukumar Mishra")
                .withField(StandardField.DATE, "25 2 2019")
                .withField(StandardField.YEAR, "2019")
                .withField(StandardField.DOI, "10.1049/iet-rpg.2018.5648")
                .withField(StandardField.FILE, ":https\\://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=8636659:PDF")
                .withField(StandardField.ISSUE, "3")
                .withField(StandardField.ISSN, "1752-1424")
                .withField(StandardField.JOURNALTITLE, "IET Renewable Power Generation")
                .withField(StandardField.PAGES, "418--426")
                .withField(StandardField.PUBLISHER, "IET")
                .withField(StandardField.TITLE, "Optimal operation of PV-DG-battery based microgrid with power quality conditioner")
                .withField(StandardField.VOLUME, "13");

        List<BibEntry> fetchedEntries = fetcher.performSearch("8636659"); // article number
        fetchedEntries.forEach(entry -> entry.clearField(StandardField.ABSTRACT)); // Remove abstract due to copyright);
        assertEquals(Collections.singletonList(expected), fetchedEntries);
    }

    @Test
    void searchByQueryFindsEntry() throws Exception {
        BibEntry expected = new BibEntry(StandardEntryType.InProceedings)
                .withField(StandardField.AUTHOR, "Igor Steinmacher and Tayana Uchoa Conte and Christoph Treude and Marco Aurélio Gerosa")
                .withField(StandardField.DATE, "14-22 May 2016")
                .withField(StandardField.YEAR, "2016")
                .withField(StandardField.EVENTDATE, "14-22 May 2016")
                .withField(StandardField.EVENTTITLEADDON, "Austin, TX")
                .withField(StandardField.LOCATION, "Austin, TX")
                .withField(StandardField.DOI, "10.1145/2884781.2884806")
                .withField(StandardField.JOURNALTITLE, "2016 IEEE/ACM 38th International Conference on Software Engineering (ICSE)")
                .withField(StandardField.PAGES, "273--284")
                .withField(StandardField.ISBN, "978-1-5090-2071-3")
                .withField(StandardField.ISSN, "1558-1225")
                .withField(StandardField.PUBLISHER, "IEEE")
                .withField(StandardField.KEYWORDS, "Portals, Documentation, Computer bugs, Joining processes, Industries, Open source software, Newcomers, Newbies, Novices, Beginners, Open Source Software, Barriers, Obstacles, Onboarding, Joining Process")
                .withField(StandardField.TITLE, "Overcoming Open Source Project Entry Barriers with a Portal for Newcomers")
                .withField(StandardField.FILE, ":https\\://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=7886910:PDF")
                .withField(StandardField.ABSTRACT, "Community-based Open Source Software (OSS) projects are usually self-organized and dynamic, receiving contributions from distributed volunteers. Newcomer are important to the survival, long-term success, and continuity of these communities. However, newcomers face many barriers when making their first contribution to an OSS project, leading in many cases to dropouts. Therefore, a major challenge for OSS projects is to provide ways to support newcomers during their first contribution. In this paper, we propose and evaluate FLOSScoach, a portal created to support newcomers to OSS projects. FLOSScoach was designed based on a conceptual model of barriers created in our previous work. To evaluate the portal, we conducted a study with 65 students, relying on qualitative data from diaries, self-efficacy questionnaires, and the Technology Acceptance Model. The results indicate that FLOSScoach played an important role in guiding newcomers and in lowering barriers related to the orientation and contribution process, whereas it was not effective in lowering technical barriers. We also found that FLOSScoach is useful, easy to use, and increased newcomers' confidence to contribute. Our results can help project maintainers on deciding the points that need more attention in order to help OSS project newcomers overcome entry barriers.");
        List<BibEntry> fetchedEntries = fetcher.performSearch("Overcoming Open Source Project Entry Barriers with a Portal for Newcomers");
        assertEquals(Collections.singletonList(expected), fetchedEntries);
    }

    @Override
    public SearchBasedFetcher getFetcher() {
        return fetcher;
    }

    @Override
    public List<String> getTestAuthors() {
        return List.of("Igor Steinmacher", "Tayana Uchoa Conte", "Christoph Treude", "Marco Aurélio Gerosa");
    }

    @Override
    public String getTestJournal() {
        return "IET Renewable Power Generation";
    }

    @Override
    public PagedSearchBasedFetcher getPagedFetcher() {
        return fetcher;
    }
}
