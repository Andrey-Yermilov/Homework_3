import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Search on job page and analyse results
 */
public class SearchTest {
    WebDriver driver;
    String url = "http://www.tut.by/";
    String targetLinkTemplate = "//a[@class='topbar__link' and @title='%s']";
    String button = "navi-search-button";
    String field = "//input[@data-qa='vacancy-serp__query']";
    String resultTitle = "//a[@data-qa='vacancy-serp__vacancy-title']";

    @BeforeTest
    /**
     * Method opens browser and go to start url
     */
    public void setUp() {
        driver = new FirefoxDriver();
        driver.get(url);
        driver.manage().window().maximize();
    }

    @Test
    /**
     * Method performs search, counts and logs results
     */
    public void testSearchResults(){
        String targetLink = "Работа";
        navigateToPage(targetLink);
        String search_request = "специалист по тестированию";
        performSearch (search_request);
        List<WebElement> searchResults = driver.findElements(By.xpath(resultTitle));
        SearchResultsAnalyser searchResultsAnalyser = countSearchResult(searchResults);
        logResults(searchResultsAnalyser.getTotal(),searchResultsAnalyser.getCorrect(), searchResultsAnalyser.getIncorrect());
    }

    /**
     * Method performs search
     */
    public void performSearch (String search_request){
        WebElement fldInput = driver.findElement(By.xpath(field));
        WebElement btnSearch = driver.findElement(By.className(button));
        fldInput.sendKeys(search_request);
        btnSearch.click();
    }

    /**
     * Method opens target page
     * @param targetLink Name of link should be opened
     */
    public void navigateToPage (String targetLink){
        WebElement targetPage = driver.findElement(By.xpath(String.format(targetLinkTemplate,targetLink)));
        targetPage.click();
    }

    /**
     * Object of this class has 3 field: total amount of results, amount of correct results, amount of incorrect results
     */
    public class SearchResultsAnalyser {
        private int correct = 0;
        private int incorrect = 0;
        private int total=0;

        public int getCorrect() {
            return correct;
        }

        public void setCorrect(int correct) {
            this.correct = correct;
        }

        public int getIncorrect() {
            return incorrect;
        }

        public void setIncorrect(int incorrect) {
            this.incorrect = incorrect;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }

    /**
     * Method calculates total amount of search results, amount of correct results, amount of incorrect results
     * @param searchResults is a List<WebElement> of search results
     */
    SearchResultsAnalyser countSearchResult (List<WebElement> searchResults) {
        SearchResultsAnalyser searchResultsAnalyser = new SearchResultsAnalyser();
        searchResultsAnalyser.setTotal(searchResults.size());
        int correct=0;
        int incorrect=0;
        for (WebElement element : searchResults) {
            String result = element.getText().toLowerCase();
            if (result.contains("специалист") && result.contains("по") && result.contains("тестировани")) {
                correct++;
            } else {
                incorrect++;
            }
        }
        searchResultsAnalyser.setCorrect(correct);
        searchResultsAnalyser.setIncorrect(incorrect);
        return searchResultsAnalyser;
    }
    /**
     * Print results
     * @param total     The total amount of  displayed results
     * @param correct   The total amount of  results, which are matched test request
     * @param incorrect The total amount of  results, which are not matched test request
     */
    public  void logResults(int total, int correct, int incorrect){
        System.out.println("---------------------");
        System.out.printf("%-19s%2d%n", "Total results:", total);
        System.out.printf("%-19s%2d%n", "Correct results:", correct);
        System.out.printf("%-19s%2d%n", "Incorrect results:", incorrect);
        System.out.println("---------------------");
    }

    @AfterTest
    /**
     * Method closes browser
     */
    public void tearDown() {
        driver.quit();
    }
}
