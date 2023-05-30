package actions.commons;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.util.List;
import java.util.Set;

public abstract class AbstractPage {

    private Alert alert;
    private Select select;
    private Actions action;
    private long longTimeout = 30;
    private WebDriverWait explicitWait;
    private JavascriptExecutor jsExecutor;

    public void openUrl(WebDriver driver, String url) {
        // Open the URL
        driver.get(url);
    }

    public String getPageTitle(WebDriver driver) {
        // Get the Page Title
        return driver.getTitle();
    }

    public String getCurrentPageUrl(WebDriver driver) {
        // Get the Current Page URL
        return driver.getCurrentUrl();
    }

    public String getPageSource(WebDriver driver) {
        // Get the Page Source
        return driver.getPageSource();
    }

    public void backToPage(WebDriver driver) {
        // Go Back to the Page
        driver.navigate().back();
    }

    public void forwardToPage(WebDriver driver) {
        // Go Forward to the Page
        driver.navigate().forward();
    }

    public void refreshCurrentPage(WebDriver driver) {
        // Refresh the Page
        driver.navigate().refresh();
    }

    public void waitToAlertPresence(WebDriver driver) {
        explicitWait = new WebDriverWait(driver , longTimeout);
        explicitWait.until(ExpectedConditions.alertIsPresent());
    }

    public void acceptAlert(WebDriver driver) {
        alert = driver.switchTo().alert();
        alert.accept();
    }

    public void cancelAlert(WebDriver driver) {
        alert = driver.switchTo().alert();
        alert.dismiss();
    }

    public void sendKeyToAlert(WebDriver driver, String value) {
        alert = driver.switchTo().alert();
        alert.sendKeys(value);
    }

    public String getTextAlert(WebDriver driver) {
        alert = driver.switchTo().alert();
        return alert.getText();
    }

    public void switchToWindowByID(WebDriver driver, String parentID) {
        Set<String> allWindows = driver.getWindowHandles();
        for (String runWindow : allWindows) {
            if (!runWindow.equals(parentID)) {
                driver.switchTo().window(runWindow);
                break;
            }
        }
    }

    public void switchToWindowByTitle(WebDriver driver, String title) {
        Set<String> allWindows = driver.getWindowHandles();
        for (String runWindow : allWindows) {
            driver.switchTo().window(runWindow);
            String currentWindow = driver.getTitle();
            if (currentWindow.equals(title)) {
                break;
            }
        }
    }

    public void closeAllWindowsWithoutParent(WebDriver driver, String parentID) {
        Set<String> allWindows = driver.getWindowHandles();
        for (String runWindow : allWindows) {
            if (!runWindow.equals(parentID)) {
                driver.switchTo().window(runWindow);
                driver.close();
            }
        }

        driver.switchTo().window(parentID);
    }

    public By byXpath(String locator) {
        return By.xpath(locator);
    }

    public WebElement find(WebDriver driver, String locator) {
        return driver.findElement(byXpath(locator));
    }

    public List<WebElement> finds(WebDriver driver, String locator) {
        return driver.findElements(byXpath(locator));
    }

    public void clickToElement(WebDriver driver, String locator) {
        find(driver , locator);
    }

    public void sendKeysToElement(WebDriver driver, String locator, String value) {
        find(driver , locator).clear();
        find(driver, locator).sendKeys(value);
    }

    public void selectItemInDropdown(WebDriver driver, String locator, String itemValue) {
        select = new Select(find(driver, locator));
        select.selectByVisibleText(itemValue);
    }

    public String getFirstSelectedItemInDropdown(WebDriver driver, String locator) {
        select = new Select(find(driver, locator));
        return select.getFirstSelectedOption().getText();
    }

    public boolean isDropdownMultiple(WebDriver driver, String locator) {
        select = new Select(find(driver, locator));
        return select.isMultiple();
    }

    public void selectItemInCustomDrown(WebDriver driver, String parentLocator, String childItemLocator, String expectedItem) {
        // Click to parent locator
        find(driver, parentLocator).click();
        sleepInSecond(1);

        // Wait to child item is show
        explicitWait = new WebDriverWait(driver , longTimeout);
        explicitWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(byXpath(childItemLocator)));

        // Give all item in dropdown to list to check
        List<WebElement> items = finds(driver, childItemLocator);

        // Run through ask the values in the list
        for (WebElement item : items) {
            // Check text of values equal expected Item
            if (item.getText().equals(expectedItem)) {
                // Scroll down to item
                jsExecutor = (JavascriptExecutor) driver;
                jsExecutor.executeScript("arguments[0].scrollIntoView(true);", item);
                sleepInSecond(1);

                // Click to item
                item.click();
                sleepInSecond(1);
                break;
            }
        }
    }

    public void sleepInSecond(long timeout) {
        try {
            Thread.sleep(timeout * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getElementAttribute(WebDriver driver, String locator, String attributeName) {
        return find(driver, locator).getAttribute(attributeName);
    }

    public String getElementText(WebDriver driver, String locator) {
        return find(driver, locator).getText();
    }

    public int countElementSize(WebDriver driver, String locator) {
        return finds(driver, locator).size();
    }

    public void checkToCheckbox(WebDriver driver, String locator) {
        if (!find(driver, locator).isSelected()) {
            find(driver, locator).click();
        }
    }

    public void uncheckToCheckbox(WebDriver driver, String locator) {
        if (find(driver, locator).isSelected()) {
            find(driver, locator).click();
        }
    }

    public boolean isControlDisabled(WebDriver driver, String locator) {
        return find(driver, locator).isEnabled();
    }

    public boolean isControlEnabled(WebDriver driver, String locator) {
        return find(driver, locator).isDisplayed();
    }

    public boolean isControlSelected(WebDriver driver, String locator) {
        return find(driver, locator).isSelected();
    }

    public void switchToFrame(WebDriver driver, String locator) {
        driver.switchTo().frame(find(driver, locator));
    }

    public void switchToDefaultPage(WebDriver driver) {
        driver.switchTo().defaultContent();
    }

    public void doubleClickToElement(WebDriver driver, String locator) {
        action = new Actions(driver);
        action.doubleClick(find(driver, locator)).perform();
    }

    public void rightClickToElement(WebDriver driver, String locator) {
        action = new Actions(driver);
        action.contextClick(find(driver, locator)).perform();
    }

    public void hoverToElement(WebDriver driver, String locator) {
        action = new Actions(driver);
        action.moveToElement(find(driver, locator)).perform();
    }

    public void dragAndDropElement(WebDriver driver, String sourceLocator, String targetLocator) {
        action = new Actions(driver);
        action.dragAndDrop(find(driver, sourceLocator) , find(driver, targetLocator)).perform();
    }

    public void sendKeyBoardToElement(WebDriver driver, String locator, Keys key) {
        action = new Actions(driver);
        action.sendKeys(find(driver , locator) , key).perform();
    }

    public Object executeForBrowser(WebDriver driver, String javaScript) {
        jsExecutor = (JavascriptExecutor) driver;
        return jsExecutor.executeScript(javaScript);
    }

    public String getInnerText(WebDriver driver) {
        jsExecutor = (JavascriptExecutor) driver;
        return (String) jsExecutor.executeScript("return document.documentElement.innerText;");
    }

    public boolean areExpectedTextInInnerText(WebDriver driver, String textExpected) {
        jsExecutor = (JavascriptExecutor) driver;
        String textActual = (String) jsExecutor.executeScript("return document.documentElement.innerText.match('" + textExpected + "')[0]");
        return textActual.equals(textExpected);
    }

    public void scrollToBottomPage(WebDriver driver) {
        jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("window.scrollBy(0,document.body.scrollHeight)");
    }

    public void navigateToUrlByJS(WebDriver driver, String url) {
        jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("window.location = '" + url + "'");
    }

    public void highlightElement(WebDriver driver, String locator) {
        jsExecutor = (JavascriptExecutor) driver;
        WebElement element = find(driver, locator);
        String originalStyle = element.getAttribute("style");
        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", "border: 2px solid red; border-style: dashed;");
        sleepInSecond(1);
        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", originalStyle);
    }

    public void clickToElementByJS(WebDriver driver, String locator) {
        jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].click();", find(driver, locator));
    }

    public void scrollToElement(WebDriver driver, String locator) {
        jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", find(driver, locator));
    }

    public void sendKeyToElementByJS(WebDriver driver, String locator, String value) {
        jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].setAttribute('value', '" + value + "')", find(driver, locator));
    }

    public void removeAttributeInDOM(WebDriver driver, String locator, String attributeRemove) {
        jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].removeAttribute('" + attributeRemove + "');", find(driver, locator));
    }

    public boolean areJQueryAndJSLoadedSuccess(WebDriver driver) {
        explicitWait = new WebDriverWait(driver, longTimeout);
        jsExecutor = (JavascriptExecutor) driver;

        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return ((Long) jsExecutor.executeScript("return jQuery.active") == 0);
                } catch (Exception e) {
                    return true;
                }
            }
        };

        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return jsExecutor.executeScript("return document.readyState").toString().equals("complete");
            }
        };

        return explicitWait.until(jQueryLoad) && explicitWait.until(jsLoad);
    }

    public String getElementValidationMessage(WebDriver driver, String locator) {
        jsExecutor = (JavascriptExecutor) driver;
        return (String) jsExecutor.executeScript("return arguments[0].validationMessage;", find(driver, locator));
    }

    public boolean isImageLoaded(WebDriver driver, String locator) {
        jsExecutor = (JavascriptExecutor) driver;
        boolean status = (boolean) jsExecutor.executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0", find(driver, locator));
        if (status) {
            return true;
        } else {
            return false;
        }
    }

    public void waitToElementPresence(WebDriver driver, String locator) {
        explicitWait = new WebDriverWait(driver, longTimeout);
        explicitWait.until(ExpectedConditions.presenceOfElementLocated(byXpath(locator)));
    }


    public void waitToElementVisible(WebDriver driver, String locator) {
        explicitWait = new WebDriverWait(driver, longTimeout);
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(byXpath(locator)));
    }

    public void waitToElementInvisible(WebDriver driver, String locator) {
        explicitWait = new WebDriverWait(driver, longTimeout);
        explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(byXpath(locator)));
    }

    public void waitToElementClickable(WebDriver driver, String locator) {
        explicitWait = new WebDriverWait(driver, longTimeout);
        explicitWait.until(ExpectedConditions.elementToBeClickable(byXpath(locator)));
    }
}

