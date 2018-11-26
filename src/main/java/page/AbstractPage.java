package page;

import com.google.common.base.Predicate;
import config.MainConf;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import ru.yandex.qatools.allure.annotations.Attachment;
import util.JsonParser;

/**
 * Базовая страница, содержащая общие методы и поля
 */
public abstract class AbstractPage {

    /**
     * Веб-драйвер
     */
    WebDriver driver;

    /**
     * Xpath-шаблоны
     */
    private Map<String, String> templates;

    /**
     * Инициализация страницы из конфигураций
     * @param fileName расположение конфигурации xpath-шаблонов
     * @param urlName адрес нужной страницы
     * @param driver веб-драйвер
     */
    public void init(String fileName, String urlName, WebDriver driver) {
        templates = JsonParser.fromJson(HashMap.class, fileName);
        this.driver = driver;
        driver.get(MainConf.getUrl(urlName));
    }

    String getTemplate(String name) {
        return templates.get(name);
    }

    /**
     * Метод обновления страницы
     */
    public void refresh() {
        driver.navigate().refresh();
    }

    /**
     * Метод приложения текстовой информации в отчет
     */
    @Attachment("Step Info")
    public String putInfo(String text) {
        return text;
    }

    /**
     * Метод ожидания анимации элемента
     * Необходим для избежания ошибок из некорректной позиции элемента во время анимации
     */
    void waitAnimation(String elementId) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        String query = String.format("return jQuery(\"%s\").is(\":animated\");", elementId);
        wait.until((Predicate<WebDriver>) driver ->
                !(boolean)((JavascriptExecutor)driver).executeScript(query)
        );
    }

    /**
     * Метод поиска элемента по имени локатора
     * @param template имя локатора в конфигурации страницы
     */
    WebElement findElement(String template) {
        String locator = getTemplate(template);
        if (locator == null) {
            Assert.fail("Не найден локатор с именем \"" + template + "\"");
        }
        List<WebElement> elements = driver.findElements(By.xpath(locator));
        switch (elements.size()) {
            case 0:
                Assert.fail("Не найден элемент по локатору: " + locator);
                return null;
            case 1:
                return elements.get(0);
            default:
                Assert.fail("По локатору" + locator + "найдено более одного элемента");
                return null;
        }
    }

    /**
     * Метод получения текста элемента
     */
    String getText(String template) {
        return findElement(template).getText();
    }

    /**
     * Метод нажатия на элемент
     */
    void click(String template) {
        findElement(template).click();
    }

    /**
     * Метод ввода символов в элемент
     */
    void sendKeys(String template, String keys) {
        findElement(template).sendKeys(keys);
    }

    /**
     * Метод получения значения атрибута элемента
     */
    String getAttribute(String template, String attribute) {
        return findElement(template).getAttribute(attribute);
    }
}
