package tech.alexontest.poftutor;

import com.google.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tech.alexontest.poftutor.infrastructure.AbstractTest;
import tech.alexontest.poftutor.steps.HomePageSteps;

@Tag("Content")
class HomePageTests extends AbstractTest {

    @Inject
    private HomePageSteps homePageSteps;

    @BeforeAll
    void setupHomepage() {
        homePageSteps.loadHomePage();
    }

    @Test
    @DisplayName("Reported URL is correct")
    void correctUrlIsReported() {
        homePageSteps.assertThatReportedUrlIsCorrect();
    }

    @Test
    @DisplayName("Tab page title is correct")
    void correctTitleIsDisplayedOnTheBrowserTab() {
        homePageSteps.assertThatBrowserTabPageTitleIsCorrect();
    }

    @Test
    @DisplayName("The correct title is reported")
    void titleIsCorrect() {
        homePageSteps.assertThatTitleIsCorrect();
    }

    @Test
    @DisplayName("5 Widgets appear on the Homepage")
    void pageContainsFiveWidgets() {
        homePageSteps.assertThatPageContainsFiveWidgets();
    }

    @Test
    @DisplayName("No more than 5 posts are linked on the Homepage")
    void pageContainsUpToFiveArticles() {
        homePageSteps.assertThatPageContainsUpToFiveArticles();
    }
}
