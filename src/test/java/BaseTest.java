import config.MainConf;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import util.DriverFactory;

/**
 * Базовый тестовый класс, содержащий общие для всех тестов методы и поля
 */
public abstract class BaseTest {

    /**
     * Веб-драйвер
     */
    static final ThreadLocal<WebDriver> driver = DriverFactory.getDriver();

    /**
     * Инициализация конфигурации перед началом теста
     */
    @BeforeClass
    public static void createAndStartService() {
        MainConf.init(System.getProperty("main-conf-location"));
    }

    /**
     * Закрытие браузера по завершении тестового метода
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown(){
        driver.get().quit();
    }
}
