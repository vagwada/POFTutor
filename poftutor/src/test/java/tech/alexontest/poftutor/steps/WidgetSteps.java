/*
 * Copyright (c) 2018. Alexander Dunn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.alexontest.poftutor.steps;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;
import tech.alexontest.poftutor.pageblocks.ArchivesWidgetBlock;
import tech.alexontest.poftutor.pageblocks.CategoriesWidgetBlock;
import tech.alexontest.poftutor.pageblocks.MetaWidgetBlock;
import tech.alexontest.poftutor.pageblocks.SearchWidgetBlock;
import tech.alexontest.poftutor.pageblocks.TagCloudWidgetBlock;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.alexontest.poftutor.infrastructure.HttpTools.assertLinkIsNotBroken;

public class WidgetSteps {
    private final SearchWidgetBlock searchWidgetBlock;

    private final TagCloudWidgetBlock tagCloudWidgetBlock;

    private final CategoriesWidgetBlock categoriesWidgetBlock;

    private final ArchivesWidgetBlock archivesWidgetBlock;

    private final MetaWidgetBlock metaWidgetBlock;

    private final SearchResultsSteps searchResultsSteps;

    @Inject
    public WidgetSteps(final SearchWidgetBlock searchWidgetBlock,
                       final TagCloudWidgetBlock tagCloudWidgetBlock,
                       final CategoriesWidgetBlock categoriesWidgetBlock,
                       final ArchivesWidgetBlock archivesWidgetBlock,
                       final MetaWidgetBlock metaWidgetBlock,
                       final SearchResultsSteps searchResultsSteps) {
        this.searchWidgetBlock = searchWidgetBlock;
        this.tagCloudWidgetBlock = tagCloudWidgetBlock;
        this.categoriesWidgetBlock = categoriesWidgetBlock;
        this.archivesWidgetBlock = archivesWidgetBlock;
        this.metaWidgetBlock = metaWidgetBlock;
        this.searchResultsSteps = searchResultsSteps;
    }

    public WidgetSteps verifyYouCanEnterSearchText(final String searchText) {
        searchWidgetBlock.enterSearchText(searchText);
        assertThat(searchWidgetBlock.getSearchBarText())
                .as("Search Bar Text was not as expected")
                .isEqualTo(searchText);
        return this;
    }

    /**
     * Submit search for 'searchText' using the search button (magnifying glass icon).
     * This works regardless of if the searchText is found on the site.
     * @param searchText text to search for.
     * @return a self reference
     */
    public SearchResultsSteps search(final String searchText) {
        searchWidgetBlock.enterSearchText(searchText)
                .submitSearchByButton();
        return searchResultsSteps;
    }

    /**
     * Submit search for 'searchText' using the enter key.
     * NOTE - this will throw a NoSuchElementException if the searchText does not appear on the site.
     * @param searchText text to search for.
     * @return a self reference
     */
    public SearchResultsSteps searchByEnter(final String searchText) {
        searchWidgetBlock.searchByEnter(searchText);
        return searchResultsSteps;
    }

    public WidgetSteps assertTagCloudTitle() {
        assertThat(tagCloudWidgetBlock.getTitle())
                .as("Tag Cloud widget title is incorrect")
                .isEqualToIgnoringCase("Tags");
        return this;
    }

    public WidgetSteps assertMostProminentTags() {
        final List<String> mostImportantTags = ImmutableList.of(
                "Automated Testing",
                "WebDriver",
                "Selenium",
                "Selenium WebDriver",
                "JUnit5",
                "Java");
        assertThat(mostImportantTags)
                .as("The most important Tags are prominent")
                .isSubsetOf(tagCloudWidgetBlock.getProminentTags());
        return this;
    }

    public WidgetSteps verifyCategoriesWidgetLayout() {
        final List<String> expectedCategories = ImmutableList.of(
                "DevOps",
                "PageFactory Tutorial",
                "Software Development Life Cycle",
                "Technology Updates",
                "WordPress Implementation"
        );

        assertThat(categoriesWidgetBlock.getTitle())
                .as("Categories widget title is incorrect")
                .isEqualToIgnoringCase("Categories");

        assertThat(expectedCategories)
                .as("The most important Tags are prominent")
                .isSubsetOf(categoriesWidgetBlock.getCategories());
        return this;
    }

    public WidgetSteps verifyArchivesWidgetLayout() {

        assertThat(archivesWidgetBlock.getTitle())
                .as(" Archives widget title is incorrect")
                .isEqualToIgnoringCase("Archives");

        assertThat(archivesWidgetBlock.getMonths().size())
                .as("Some months tags appear")
                .isGreaterThanOrEqualTo(9);
        return this;
    }

    public WidgetSteps verifyArchivesLinksAreValid() {
        //This link checking is intrinsically slow. Parallelism here halves the time taken.
        archivesWidgetBlock.getMonths().parallelStream()
                .forEach(p -> assertLinkIsNotBroken(p.getRight()));
        return this;
    }

    public WidgetSteps verifyMetaWidgetLayout() {
        final List<String> expectedLinks = ImmutableList.of(
                "Register",
                "Log in",
                "Entries RSS",
                "Comments RSS",
                "WordPress.org");

        assertThat(metaWidgetBlock.getTitle())
                .as("Meta widget title is incorrect")
                .isEqualToIgnoringCase("Meta");

        final List<String> actualLinks = metaWidgetBlock.getLinks()
                .stream()
                .map(Pair::getLeft)
                .collect(Collectors.toList());

        assertThat(expectedLinks)
                .as("The expected Links are not displayed in the Meta Widget")
                .containsAll(actualLinks);
        return this;
    }
}
