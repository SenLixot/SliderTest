package page;

import config.Tariff;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import ru.yandex.qatools.allure.annotations.Step;

/**
 * Заглавная страница
 */
public class IndexPage extends AbstractPage {

    /**
     * Метод получения данных о текущем тарифе
     */
    public Tariff getCurrentTariff() {
        Integer time = Integer.parseInt(getText("Срок текущего тарифа"));
        String speed = getText("Скорость текущего тарифа");
        Integer cost = Integer.parseInt(getText("Цена текущего тарифа"));
        return new Tariff(time, speed, cost);
    }

    /**
     * Метод получения данных о предложенном тарифе
     */
    public Tariff getOfferedTariff() {
        Integer time = Integer.parseInt(getText("Срок предложенного тарифа"));
        String speed = getText("Скорость предложенного тарифа");
        Integer cost = Integer.parseInt(getText("Цена предложенного тарифа"));
        return new Tariff(time, speed, cost);
    }

    /**
     * Метод перемещения ползунка влево(нажатие на "-")
     */
    @Step("Перемещение ползунка влево")
    public void clickDecrease() {
        click("Кнопка -");
        waitSlider();
    }

    /**
     * Метод перемещения ползунка вправо(нажатие на "+")
     */
    @Step("Перемещение ползунка вправо")
    public void clickIncrease() {
        WebElement element = findElement("Кнопка +");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element); //костыль, так как "плюсик" не кликается по центру
        waitSlider();
    }

    /**
     * Метод пополнения баланса на объем, равным цене предложенного тарифа
     */
    @Step("Пополнение баланса")
    public void addFundsByTariff(Tariff tariff) {
        putInfo("Пополнение на сумму: " + String.valueOf(tariff.getCost()));
        sendKeys("Поле пополнения", String.valueOf(tariff.getCost()));
        click("Кнопка подтверждения пополнения");
    }

    /**
     * Метод пополнения баланса на произвольный объем
     */
    @Step("Пополнение баланса")
    public void addFunds(Integer amount) {
        putInfo("Пополнение на сумму: " + String.valueOf(amount));
        sendKeys("Поле пополнения", String.valueOf(amount));
        click("Кнопка подтверждения пополнения");
    }

    /**
     * Метод получения баланса
     */
    public Integer getBalance() {
        return Integer.parseInt(getText("Баланс"));
    }

    /**
     * Метод проверки доступности кнопки подключения к тарифу
     */
    public boolean isPurchaseAvailable() {
        String buttonClass = getAttribute("Кнопка подключения", "class");
        return !buttonClass.contains("disabled");
    }

    /**
     * Метод нажатия на кнопку подключения к тарифу
     */
    @Step("Подключение к тарифу")
    public void purchase() {
        click("Кнопка подключения");
    }

    /**
     * Метод нажатия на кнопку "Сброс"
     */
    @Step("Сброс")
    public void reset() {
        click("Кнопка сброса");
    }

    /**
     * Метод получения позиции ползунка
     */
    public Double getSliderPosition() {
        String style = getAttribute("Ползунок", "style");
        Pattern pattern = Pattern.compile("-?\\d+.?\\d");
        Matcher matcher = pattern.matcher(style);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(0));
        } else {
            Assert.fail("Не удалось получить позицию ползунка");
            return null;
        }
    }

    /**
     * Метод для выяснения был ли ползунок перемещен
     * @param sliderPosBefore позиция ползунка до каких-либо действий
     * @param sliderPosAfter позиция ползунка после каких-либо действий
     */
    public boolean sliderMoved(double sliderPosBefore, double sliderPosAfter) {
        return !(sliderPosBefore == sliderPosAfter);
    }

    /**
     * Метод ожидания анимации ползунка
     */
    public void waitSlider() {
        String sliderId = getTemplate("Ползунок (id)");
        waitAnimation(sliderId);
    }

}
